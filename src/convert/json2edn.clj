#!/usr/bin/env bb

(require '[clojure.pprint :refer [pprint]])
(require '[clojure.set :as set])

(defn keywordise [str]
  (keyword (-> (str/lower-case str)
               (str/replace "." "")
               (str/replace " " "-"))))

(defn process [path hook]
  (let [out-path (str/replace path #".json$" ".edn")]
    (println (str "~ Writing " out-path "."))
    (let [record (json/parse-string (slurp path) true)
          result (hook record)]
      (spit out-path (with-out-str (pprint result))))))

(process "src/data/hs2022.json" #(:results %))
