#!/usr/bin/env clojure -M

(require '[fipp.edn :refer [pprint]])
(require '[puget.printer :as puget])
(require '[clojure.edn :as edn])
(require '[clojure.set :as set])
(require '[clojure.string :as str])
(require '[clojure.walk :refer [postwalk]])
(require '[babashka.process :refer [shell]])

; Next step:
; - Sort alphabetically & filter out possible duplicates in keywords.
; - Delete extra? Probably yes for this current version.
; - Convert to JSON.

(def data-path "public/workers/autocomplete/data.edn")
(def records (atom (edn/read-string (slurp data-path))))

(defn save-results []
  (pprint @records {:writer (clojure.java.io/writer data-path)}))

(defn commit [record]
  (let [message (str "Keywords for " (:code record) " " (:label record))]
    (shell {:out :string :err :string} "git" "commit" data-path "-m" message)))

(defn my-sorted-set [coll]
  (apply sorted-set-by #(compare (str/lower-case %1) (str/lower-case %2)) coll))

(defn readline [label]
  (print (str label ": ")) (flush)
  (my-sorted-set (remove empty? (str/split (read-line) #"\s*,\s*"))))

(defn edit-record [cursor record]
  (when-not record
    (throw (ex-info "Empty record" {:cursor cursor})))

  (puget/cprint (select-keys record [:code :label]))
  (println)
  (puget/cprint
   (postwalk (fn [i]
               (when (not (and (coll? i)
                               (= (count i) 2)
                               (= (first i) :code))) i))
             (:extra record)))
  (println)

  (let [syn (readline "syn")
        rel (readline "rel")
        exc (readline "exc")]

    (reset! records (assoc-in @records (conj cursor :syn) syn))
    (reset! records (assoc-in @records (conj cursor :rel) rel))
    (reset! records (assoc-in @records (conj cursor :exc) exc))

    ; Do in a background thread.
    ; TODO: Don't exit until results saved.
    ; Is exc useful at all?
    ; Review data.edn
    ; Progress counter: how many items of total are done.
    ; Are we doing L4 as well?
    ; Document this. For instance seed in L4 has weight, but less than L6 rel and that is still less than L6 syn.
    ; Write the algorythm (for the documentation) of how the matching will work.
    (future
      (save-results)
      (commit record))))

(defn post-process [cursor record]
  (when (not (vector? (get-in @records cursor)))
    (reset! records (assoc-in @records (conj cursor :syn) (my-sorted-set (:syn record)))))
  (when (not (vector? (get-in @records cursor)))
    (reset! records (assoc-in @records (conj cursor :rel) (my-sorted-set (:rel record)))))
  (when (not (vector? (get-in @records cursor)))
    (reset! records (assoc-in @records (conj cursor :exc) (my-sorted-set (:exc record))))))

(defn get-cursors [item cursor]
  (cond
    (and (map? item) (not (re-find #"^\d+\.\d+\.\d+$" (or (:code item) ""))))
    (map-indexed
     #(get-cursors %2 (conj cursor :items %1))
     (:items item))

    (and (map? item) (re-find #"^\d+\.\d+\.\d+$" (or (:code item) "")))
    cursor

    (coll? item)
    (map-indexed
     #(get-cursors %2 (conj cursor %1))
     item)))

(let [cursors (get-cursors @records [])]
  (postwalk (fn [i]
              (if (and (vector? i)
                       (= (count i) 5))
                (let [record (get-in @records i)]
                  (if (empty? (apply concat (vals (select-keys record [:syn :rel :exc]))))
                    (edit-record i record)
                    (post-process i record)))
                i))
            cursors))
