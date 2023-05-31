#!/usr/bin/env bb

; csv2edn.clj src/data/cpa.csv > src/data/cpa.edn
(defn exit-with-error [message]
  (throw (ex-info
          (str \u001b "[31m" "Error: " \u001b "[0m" message)
          {:babashka/exit 1})))

(when-not (= (count *command-line-args*) 1)
  (exit-with-error "only 1 argument (input file path) expected."))

(def input-file-path (first *command-line-args*))

(with-open [reader (io/reader input-file-path)]
  (print "[")
  (doseq [i (drop 1 (csv/read-csv reader))]
    (print (str " " (prn-str (update-in i [1] #(Integer/parseInt %))))))
  (println "]"))
