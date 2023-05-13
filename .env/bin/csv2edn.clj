#!/usr/bin/env bb

(defn exit-with-error [message]
  (throw (ex-info
          (str \u001b "[31m" "Error: " \u001b "[0m" message)
          {:babashka/exit 1})))

(when-not (= (count *command-line-args*) 1)
  (exit-with-error "only 1 argument (input file path) expected."))

(def input-file-path (first *command-line-args*))

; 4 is the industry level, 6 would be the product level.
(defn process-heading [row]
  {:level (Integer/parseInt (second row))
   :code (nth row 3) :label (nth row 4)})

; Whether we add empty extra or not, we always get size of 640K.
(defn process-specific-category [row]
  {:level (Integer/parseInt (second row))
   :code (nth row 3) :label (nth row 4)
   :extra (str/trim (str (nth row 5) " " (nth row 6)))}

  ;; (let [base {:level (Integer/parseInt (second row))
  ;;             :code (nth row 3) :label (nth row 4)}
  ;;       extra (str/trim (str (nth row 5) " " (nth row 6)))]
  ;;   (if (empty? extra) base (assoc base :extra extra)))
  ,)

(defn process-row [row]
  (cond
    (= (second row) "1") (process-heading row)
    (= (second row) "6") (process-specific-category row)
    true nil))

(defn process-data [data]
  (reduce (fn [acc item]
            (if (= (:level item) 4)
              (conj acc (assoc (dissoc item :level) :items []))
              (conj (butlast acc)
                    (update-in (last acc) [:items] #(conj % (dissoc item :level))))))
          []
          (filter identity (map process-row data))))

(with-open [reader (io/reader input-file-path)]
  (println (json/generate-string
            (filter (fn [l4-item]
                      (not (empty? (:items l4-item))))
                    (process-data (csv/read-csv reader))))))
