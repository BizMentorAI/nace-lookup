#!/usr/bin/env bb

(def prodcom-records (edn/read-string (slurp "src/data/prodcom2022-structure.edn")))

; L1, L2, L3 exists.
;(prn (into #{} (filter #(= (:level %) 3) prodcom-records)))
;(prn (map #(vec [(:cpa %), (:prd2022_code %)]) (filter #(= (:level %) 3) prodcom-records)))

; (nest records)
(def results
  (->> prodcom-records
   (filter #(= (:level %) 3))
   (map #(select-keys % [:nace :cpa :prd :en]))))

(spit
 "results.json" ;"public/workers/autocomplete/data.json"
 (json/generate-string results {:pretty true}))
