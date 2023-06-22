#!/usr/bin/env bb

(require '[clojure.pprint :refer [pprint]])
(require '[clojure.edn :as edn])
(require '[clojure.walk :refer [postwalk]])
(require '[babashka.process :refer [shell]])

; TODO: Do manufacturing first.

(def data-path "public/workers/autocomplete/data.edn")
;(def result-path "public/workers/autocomplete/data.json")
(def records (atom (edn/read-string (slurp data-path))))

(defn save-results []
  (spit data-path (with-out-str (pr-str @records))))

(defn commit [record]
  (let [message (str "Keywords for " (:code record) " " (:label record))]
    (shell "git" "commit" data-path "-m" message)))

(defn readline [label]
  (print (str label ": ")) (flush)
  (remove empty? (str/split (read-line) #"\s*,\s*")))

(defn edit-record [cursor record]
  (when-not record
    (throw (ex-info "Empty record" {:cursor cursor})))

  ; TODO: save into a TMP file and open in Vim?
  (pprint record)
  (println)

  (let [syn (readline "syn")
        rel (readline "rel")
        exc (readline "exc")]

    (reset! records (assoc-in @records (conj cursor :syn) syn))
    (reset! records (assoc-in @records (conj cursor :rel) rel))
    (reset! records (assoc-in @records (conj cursor :exc) exc))

    (save-results)
    (commit record)))

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
