#!/usr/bin/env bb

(require '[clojure.pprint :refer [pprint]])
(require '[clojure.edn :as edn])
(require '[clojure.walk :refer [postwalk]])
(require '[babashka.process :refer [shell]])

(def data-path "public/workers/autocomplete/data.edn")
(def result-path "public/workers/autocomplete/data.json")
(def records (atom (edn/read-string (slurp data-path))))

(defn save-results []
  (spit result-path (json/generate-string @records {:pretty true})))

(defn commit [record]
  (let [message (str "Keywords for " (:code record) " " (:label record))]
    (shell "git" "commit" result-path "-m" message)))

(defn readline [label]
  (println (str label ": "))
  (remove empty? (str/split (read-line) #"\s*,\s*")))

; TODO: Git commit with title.
(defn edit-record [cursor]
  (let [record (get-in @records cursor)]
    (when-not record
      (throw (ex-info "Empty record" {:cursor cursor})))

    (pprint record)
    (println)

    (let [syn (readline "syn")
          rel (readline "rel")
          exc (readline "exc")]
      (reset! records (assoc-in @records (conj cursor :syn) syn))
      (reset! records (assoc-in @records (conj cursor :rel) rel))
      (reset! records (assoc-in @records (conj cursor :exc) exc))

      (save-results)
      (commit record))))

(defn get-cursors [item cursor]
  (cond
    (and (map? item) (not (= (:level item) 3)))
    (map-indexed
     #(get-cursors %2 (conj cursor :items %1))
     (:items item))

    (and (map? item) (= (:level item) 3))
    cursor

    (coll? item)
    (map-indexed
     #(get-cursors %2 (conj cursor %1))
     item)))

(let [cursors (get-cursors @records [])]
  (postwalk (fn [i]
              (if (and (vector? i)
                       (= (count i) 5))
                (edit-record i)
                i))
            cursors))
