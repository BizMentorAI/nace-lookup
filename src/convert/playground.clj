#!/usr/bin/env bb

(def cpa-records (edn/read-string (slurp "src/data/cpa.edn")))
(def cpc-map-table (edn/read-string (slurp "src/data/cpa2cpc.edn")))
(def cpc-records (edn/read-string (slurp "src/data/cpc.edn")))
(def cpc-isic-map-table (edn/read-string (slurp "src/data/cpc2isic.edn")))
(def isic-naics-map-table (edn/read-string (slurp "src/data/isic2naics.edn")))
(def naics-records-index (edn/read-string (slurp "src/data/naics-index.edn")))
(def naics-records-desc (edn/read-string (slurp "src/data/naics-desc.edn")))

; NAICS --> ISIC
(def naics-codes-index (into #{} (map :code naics-records-index)))
(println (str "NAICS index: " (count naics-codes-index) " items (any level)."))

(def naics-codes-desc (into #{} (map :code naics-records-desc)))
(println (str "NAICS desc: " (count naics-codes-desc) " items (any level).\n"))

(def isic-codes (into #{} (map :isic-40 isic-naics-map-table)))
(def naics-isic-records
  (into #{} (filter naics-codes-index
                    (map :2022-naics-us isic-naics-map-table))))

(println (str "ISIC/NAICS conversion: " (count isic-naics-map-table) " map items."))
(println (str "Unique ISIC matches (against NAICS): " (count naics-isic-records) " items."))
(println (str "Unique ISIC codes: " (count isic-codes) " items.\n"))

; ISIC --> CPC
(def cpc-codes (into #{} (map :cpc-21-code cpc-isic-map-table)))
(def cpc-codes-matched (into #{} (filter isic-codes (map :isic-4-code cpc-isic-map-table))))
(println (str "All CPC codes found in the map file: " (count cpc-codes) " items."))
(println (str "Unique matches: " (count cpc-codes-matched) "\n"))

; CPC --> CPA
(def cpa-codes (into #{} (map :code (filter #(= (:level %) 6) cpa-records))))
(def cpa-codes-matched (into #{} (filter cpc-codes (map :cpc-21-code cpc-map-table))))
(println (str "All CPA codes: " (count cpa-codes) " L6 items."))
(println (str "Unique CPA matches: " (count cpa-codes-matched) " items."))
