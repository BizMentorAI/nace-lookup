#!/usr/bin/env clojure -M

(require '[fipp.edn :refer [pprint]])
(require '[puget.printer :as puget])
(require '[clojure.edn :as edn])
(require '[clojure.set :as set])
(require '[clojure.string :as str])
(require '[clojure.walk :refer [postwalk]])
(require '[babashka.process :refer [shell]])

; Next steps:
; - Review data.edn (above all rel).
; - Handle L4 items as well.
;
; - Document this. For instance seed in L4 has weight, but less than L6 rel and that is still less than L6 syn. Write the algorythm (for the documentation) of how the matching will work.
;
; - Delete extra? Probably yes for this current version.
; - Convert to JSON.

(def data-path "public/workers/autocomplete/data.edn")
(def records (atom (edn/read-string (slurp data-path))))

(defn save-results []
  (pprint @records {:writer (clojure.java.io/writer data-path)}))

(defn processed? [record]
  (empty? (apply concat (vals (select-keys record [:syn :rel :exc])))))

(defn commit [record]
  (let [message (str "Keywords for " (:code record) " " (:label record))]
    (shell {:out :string :err :string} "git" "commit" data-path "-m" message)))

(defn my-sorted-set [coll]
  (apply sorted-set-by #(compare (str/lower-case %1) (str/lower-case %2)) coll))

(defn readline [label]
  (print (str label ": ")) (flush)
  (my-sorted-set (remove empty? (map str/trim (str/split (read-line) #",")))))

(defn handle-int [fun]
  (sun.misc.Signal/handle
   (new sun.misc.Signal "INT")
   (proxy [sun.misc.SignalHandler] []
     (handle [signal] (fun)))))

(defn pause-ctrl-c []
  (handle-int #(println "~ Saving collection, wait before quitting.")))

(defn resume-ctrl-c []
  (handle-int #(System/exit 0)))

(defn body [cursor record count-info]
  (print (str count-info ": "))
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

    (future
      (pause-ctrl-c)
      (save-results)
      (commit record)
      (resume-ctrl-c))))

(defn get-records
  ([]
   (flatten (get-records @records [])))

  ([item cursor]
   (cond
     (and (map? item) (not (re-find #"^\d+\.\d+\.\d+$" (or (:code item) ""))))
     (map-indexed #(get-records %2 (conj cursor :items %1)) (:items item))

                                        ; Return the leaf node.
     (and (map? item) (re-find #"^\d+\.\d+\.\d+$" (or (:code item) "")))
     {:cursor cursor :record item}

     (coll? item)
     (map-indexed #(get-records %2 (conj cursor %1)) item))))

(defn edit-record [cursor record]
  (when-not record
    (throw (ex-info "Empty record" {:cursor cursor})))

  (let [processed-item-count
        (reduce
         (fn [acc {:keys [record]}]
           (if (processed? record) acc (inc acc)))
         0
         (get-records))

        count-info
        (str processed-item-count " of " (count (get-records)))]
    (body cursor record count-info)))

(defn post-process [cursor record]
  (when (not (vector? (get-in @records cursor)))
    (reset! records (assoc-in @records (conj cursor :syn) (my-sorted-set (:syn record)))))
  (when (not (vector? (get-in @records cursor)))
    (reset! records (assoc-in @records (conj cursor :rel) (my-sorted-set (:rel record)))))
  (when (not (vector? (get-in @records cursor)))
    (reset! records (assoc-in @records (conj cursor :exc) (my-sorted-set (:exc record))))))

(defn process [{:keys [cursor record] :as item}]
  (if (processed? record)
    (edit-record cursor record)
    (post-process cursor record)))

(doseq [item (get-records)] (process item))
