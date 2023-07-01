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

(defn readline [label]
  (print (str label ": ")) (flush)
  (vec (remove empty? (str/split (read-line) #"\s*,\s*"))))

(defn edit-record [cursor record]
  (when-not record
    (throw (ex-info "Empty record" {:cursor cursor})))

  (puget/cprint (select-keys record [:label]))
  (println)
  (puget/cprint (:extra record))
  (println)

  (let [syn (readline "syn")
        rel (readline "rel")
        exc (readline "exc")]

    (reset! records (assoc-in @records (conj cursor :syn) syn))
    (reset! records (assoc-in @records (conj cursor :rel) rel))
    (reset! records (assoc-in @records (conj cursor :exc) exc))

    ; Do in a background thread.
    (future
      (save-results)
      (commit record))))

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
                  (when (empty? (apply concat (vals (select-keys record [:syn :rel :exc]))))
                    (edit-record i record)))
                i))
            cursors))
