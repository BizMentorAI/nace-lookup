#!/usr/bin/env bb

(def cpa-records (edn/read-string (slurp "src/data/cpa.edn")))
(def cpc-map-table (edn/read-string (slurp "src/data/cpa2cpc.edn")))
(def cpc-records (edn/read-string (slurp "src/data/cpc.edn")))
(def cpc-isic-map-table (edn/read-string (slurp "src/data/cpc2isic.edn")))
(def isic-naics-map-table (edn/read-string (slurp "src/data/isic2naics.edn")))
(def naics-records-index (edn/read-string (slurp "src/data/naics-index.edn")))

(defn extend-meta [hash extra-meta]
  (with-meta hash (merge (meta hash) extra-meta)))

(defn extend-with-isic-map-items [naics-record]
  (extend-meta naics-record
               {:naics-isic-map-items
                (into #{} (filter #(= (:2022-naics-us %)
                                      (:code naics-record))
                                  isic-naics-map-table))}))

(defn extend-with-cpc-map-items [naics-record]
  (let [map-items (:naics-isic-map-items (meta naics-record))
        isic-codes (into #{} (map :isic-40 map-items))]
    (extend-meta naics-record
                 {:isic-cpc-map-items
                  ; cpc-isic-map-table
                  ; For each isic code,
                  ; find all matching cpc map items.
                  (into #{}
                        (flatten
                         (map (fn [isic-code]
                                (filter #(= (:isic-4-code %) isic-code)
                                        cpc-isic-map-table))
                              isic-codes)))})))

(defn extend-with-cpa-map-items [naics-record]
  (let [map-items (:isic-cpc-map-items (meta naics-record))
        cpc-codes (into #{} (map :cpc-21-code map-items))]
    (extend-meta naics-record
                 {:cpc-cpa-map-items
                  (into #{}
                        (flatten
                         (map (fn [cpc-code]
                                (filter #(= (:cpc-21-code %) cpc-code)
                                        cpc-map-table))
                              cpc-codes)))})))

(defn extend-with-cpa [naics-record]
  (let [map-items (:cpc-cpa-map-items (meta naics-record))
        cpa-codes (into #{} (map :cpa-21-code map-items))]
    (assoc naics-record
           :cpa-records
           (into #{}
                 (flatten (map
                           (fn [cpa-code]
                             (filter #(= (:code %) cpa-code) cpa-records))
                           cpa-codes))))))

(def naics-records
  (map #(-> %
            extend-with-isic-map-items
            extend-with-cpc-map-items
            extend-with-cpa-map-items
            extend-with-cpa)
       (filter #(re-find #"^\d+$" (:code %)) naics-records-index)))

(spit "results.json"
 ;"public/workers/autocomplete/data.json"
 ;(json/generate-string (nest cpa-records) {:pretty true})
 (json/generate-string naics-records {:pretty true}))
