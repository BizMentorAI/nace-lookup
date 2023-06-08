#!/usr/bin/env bb

(def prodcom-records (edn/read-string (slurp "src/data/prodcom2022-structure.edn")))

; L1, L2, L3 exists.
;(prn (into #{} (filter #(= (:level %) 3) prodcom-records)))
;(prn (map #(vec [(:cpa %), (:prd2022_code %)]) (filter #(= (:level %) 3) prodcom-records)))

(spit
 "results.json"
                                        ;"public/workers/autocomplete/data.json"
                                        ;(json/generate-string (nest cpa-records) {:pretty true})
 (json/generate-string prodcom-records {:pretty true}))
