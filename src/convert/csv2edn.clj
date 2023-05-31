#!/usr/bin/env bb

; csv2edn.clj src/data/cpa.csv 1 > src/data/cpa.edn
; csv2edn.clj src/data/cpc.csv 0 > src/data/cpc.edn
; csv2edn.clj src/data/cpa2cpc.csv 1,4 > src/data/cpa2cpc.edn

; TODO: write the header / maybe convert to a map.
(defn exit-with-error [message]
  (throw (ex-info
          (str \u001b "[31m" "Error: " \u001b "[0m" message)
          {:babashka/exit 1})))

(when-not (= (count *command-line-args*) 2)
  (exit-with-error "2 arguments (input file path and numeric colum index) expected."))

(def input-file-path (first *command-line-args*))

(def num-col-ind
  (map #(Integer/parseInt %)
       (str/split (last *command-line-args*) #",")))

(with-open [reader (io/reader input-file-path)]
  (print "[")
  (doseq [i (drop 1 (csv/read-csv reader))]
    (print (str " " (prn-str
                     (reduce (fn [acc ind]
                               (update-in acc [ind] #(Integer/parseInt %)))
                             i
                             num-col-ind)))))
  (println "]"))
