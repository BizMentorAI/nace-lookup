#!/usr/bin/env bb

(require '[clojure.pprint :refer [pprint]])
(require '[clojure.set :as set])

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
            results (map hook records)
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
;; (process "src/data/isic2naics.csv" identity)
;; (process "src/data/naics-desc.csv" identity)
;; (process "src/data/naics-index.csv" identity)
;; (process "src/data/prodcom2022-structure.csv"
;;          #(-> %
;;               (dissoc
;;                :fr :el :hr :pt :fi :ro :sl :es- :et :cs :hu
;;                :pl :it :nl :sk :de :sv :da :bg :lt :lv :mt)
;;               (update-in [:level] to-int)
;;               (set/rename-keys {:prd2022_code :prd})))
;; (process "src/data/cpa2cn.csv" #(dissoc % :_))
;; (process "src/data/hs-h4.csv" #(update-all (dissoc % :nomenclaturecode) [:tier] to-int))
;; (process "src/data/cpc212hs2017.csv" #(dissoc % :_))
(process "src/data/hs2022-hs2017.csv" identity)
