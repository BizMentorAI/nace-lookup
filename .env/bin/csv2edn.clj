#!/usr/bin/env bb

(defn exit-with-error [message]
  (throw (ex-info
          (str \u001b "[31m" "Error: " \u001b "[0m" message)
          {:babashka/exit 1})))

(when-not (= (count *command-line-args*) 1)
  (exit-with-error "only 1 argument (input file path) expected."))

(def input-file-path (first *command-line-args*))

; 4 is the industry level, 6 would be the product level.
(defn process-l4-l5 [row]
  (let [base {:level (Integer/parseInt (second row)) :code (nth row 3) :label (nth row 4)}
        extra (str/trim (str (nth row 5) " " (nth row 6)))]
    (if (empty? extra) base (assoc base :extra extra))))

(defn process-l6 [row]
  {:level (Integer/parseInt (second row)) :code (nth row 3) :label (nth row 4)})

(defn process-row [row]
  (cond
    (#{"4" "5"} (second row)) (process-l4-l5 row)
    (= (second row) "6") (process-l6 row)
    true nil))

(defn process-data [data]
  (reduce (fn [acc item]
            (if (= (:level item) 4)
              (conj acc (assoc item :items []))
              (conj (butlast acc)
                    (update-in (last acc) [:items] #(conj % item)))))
          []
          (filter identity (map process-row data))))

(with-open [reader (io/reader input-file-path)]
  (prn (filter (fn [l4-item]
                 (not (empty? (:items l4-item))))
               (process-data (csv/read-csv reader)))))
