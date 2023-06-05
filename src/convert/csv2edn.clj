#!/usr/bin/env bb

(require '[clojure.pprint :refer [pprint]])

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
      (let [records (csv-data->maps (csv/read-csv reader))
            results (map (fn [record] (hook record)) records)
            clean-results
            (map (fn [record]
                   (into {} (filter #(not (= (second %) "")) record)))
                 results)]
        (spit out-path (with-out-str (pprint (vec clean-results))))))))

(defn update-all [hash keys fun]
  (reduce (fn [res key] (update-in res [key] fun))
          hash
          keys))

;; (process "src/data/cpa.csv" #(update-all % [:order :level] to-int))
;; (process "src/data/cpc.csv" #(update-all % [:id] to-int))
;; (process "src/data/cpa2cpc.csv" #(update-all % [:cpa-21-count :cpc-21-count] to-int))
;; (process "src/data/unspsc.csv" #(update-all % [:id] to-int))
;; (process "src/data/cpc2isic.csv" #(dissoc % :_))
(process "src/data/isic2naics.csv" identity)
