#!/usr/bin/env bb

(defn to-int [str]
  (Integer/parseInt str))

(defn keywordise [str]
  (keyword (-> (str/lower-case str)
               (str/replace "." "")
               (str/replace " " "-"))))

(defn csv-data->maps [csv-data]
  (map zipmap
       (->> (first csv-data) ;; header
            (map keywordise)
            repeat)
	  (rest csv-data)))

(defn process [path hook]
  (let [out-path (str/replace path #".csv$" ".edn")]
    (with-open [reader (io/reader path)]
      (println (str "~ Writing " out-path "."))
      (let [results (csv-data->maps (csv/read-csv reader))]
        (spit out-path (prn-str (vec results)))))))

(process "src/data/cpa.csv" #(update-in % [1] to-int))
(process "src/data/cpc.csv" #(update-in % [0] to-int))
(process "src/data/cpa2cpc.csv" #(update-in % [1 4] to-int))
