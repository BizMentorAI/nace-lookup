#!/usr/bin/env bb

(require '[clojure.pprint :refer [pprint]])
(require '[clojure.edn :as edn])

(def cpa-records      (edn/read-string (slurp "src/data/cpa21.edn")))
(def cpc-map-table    (edn/read-string (slurp "src/data/cpa21-to-cpc21.edn")))
(def cpc-records      (edn/read-string (slurp "src/data/cpc21.edn")))
(def cn-map-table     (edn/read-string (slurp "src/data/cpa21-to-cn2023.edn")))
(def hs-map-table     (edn/read-string (slurp "src/data/cpc21-to-hs2017.edn")))
(def prodcom-records  (edn/read-string (slurp "src/data/prodcom2022-structure.edn")))
(def cn-title-records (edn/read-string (slurp "src/data/cn2023-titles.edn")))
(def cn-desc-records  (edn/read-string (slurp "src/data/cn2023.edn")))
(def hs-2017-records  (edn/read-string (slurp "src/data/hs-h4.edn")))

(defn inspect
  ([value] (prn value) value)
  ([label value] (prn label value) value))

(defn dbg [label fun]
  (fn [& args]
    (let [result (apply fun args)]
      (prn label result)
      result)))

(defn extend-meta [hash extra-meta]
  (when (or (not hash) (not extra-meta))
    (throw (ex-info "Empty argument(s)"
                    {:hash hash :extra-meta extra-meta})))
  (with-meta hash (merge (meta hash) extra-meta)))

(defmulti extend-extra
  (fn [record extra-key value]
    (when (or (not record) (not extra-key) (not value))
      (throw (ex-info "Empty argument(s)"
                      {:record record :extra-key extra-key :value value})))
    (prn :ext record extra-key value)
    (cond
      (map? record) :map (coll? record) :coll (string? record) :string
      true :other)))

(defmethod extend-extra :coll [record extra-key value]
  (throw (ex-info "fuck it" {})) ; Let's remove this body I think
  (let [values (filter (comp not empty?) value)]
    (if (not (empty? values))
      (assoc-in record [:extra extra-key]
                (str/join "\n" (remove #{"Other"} (distinct values))))
      record)))

(defn clean-value [value]
  (if (coll? value)
    (str/join "\n" (map str/trim (remove #{"Other"} (distinct value))))
    value))

(defn clean-map-from-pairs [list]
  (into {} (map (fn [[k v]] [k (clean-value v)]) list)))

; We can have a clean LIST of maps when 1:n.
; Change defmulti to match not on the record, but rather on value.
(defmethod extend-extra :map [record extra-key value]
  (let [____ (prn :v value)
        clean-pairs (remove (fn [[_ v]] (not (empty? v))) value)
        _ (prn :cp clean-pairs) ;;;;
        clean-map (clean-map-from-pairs clean-pairs)
        __ (prn :cm clean-map)] ;;;;
    (if (not (empty? clean-map))
      (assoc-in record [:extra extra-key] clean-map)
      record)))

(defmethod extend-extra :string [record extra-key value]
  (throw (ex-info "fuck it" {})) ; Let's remove this body I think
  (if (not (empty? (str/trim value)))
    (assoc-in record [:extra extra-key] value)
    record))

(defn get-cpc [{:keys [code]}]
  (let [map-record
        (first (filter #(= (:cpa-21-code %) code) cpc-map-table))
        cpc-record
        (first (filter #(= (:code %) (:cpc-21-code map-record)) cpc-records))]
    cpc-record))

(defn extend-with-cpc [record]
  (if-let [cpc-record (get-cpc record)]
    (-> record
        (extend-meta {:cpc-record cpc-record})
        (extend-extra :cpc (select-keys cpc-record [:code :title :note])))
    record))

(defn get-prodcom [{:keys [:code]}]
  (filter #(= (:cpa %) code) prodcom-records))

(defn extend-with-prodcom [record]
  (if-let [prodcom-records (get-prodcom record)]
    (let [result
          (-> record
              (extend-meta {:prodcom-records prodcom-records})
              (extend-extra :prodcom
                            (map #(select-keys % [:code :title]) prodcom-records)))]
      (prn :res result)
      result)
    record))

(defn extend-with-cn [{:keys [code] :as record}]
  (let [map-items (filter #(= (:cpa %) code) cn-map-table)
        selected-cn-title-records
        (flatten
         (map (fn [map-item]
                (filter #(= (map-item :cn) (:cn %)) cn-title-records))
              map-items))

        selected-cn-desc-records
        (flatten
         (map
          (fn [map-item]
            (filter #(= (map-item :cn) (% :code)) cn-desc-records))
          map-items))]
    (extend-extra record :cn
                  {:title (map :dm selected-cn-title-records)
                   :desc  (map :desc selected-cn-desc-records)})))

(defn extend-with-hs [record]
  (let [cpc-record (:cpc-record (meta record))
        cpc-code (:code cpc-record)
        map-records (filter #(= (:cpc-21 %) cpc-code) hs-map-table)
        selected-hs-2017-records
        (flatten (map (fn [map-record]
                        (filter #(= (str/replace (:hs-2017 map-record) #"\." "")
                                    (:code %))
                                hs-2017-records))
                      map-records))]
    (if (not (empty? selected-hs-2017-records))
      (-> record
          (extend-meta {:hs-2017-records selected-hs-2017-records})
          (extend-extra :hs {:desc (map :desc selected-hs-2017-records)}))
      record)))

; TODO: preserve the original level? Why exactly are we changing it?
(defn process-category [record]
  (case (:level record)
    1 {:level 1 :label (:desc record)}
    4 {:level 2 :code (:code record) :label (:desc record)}
    6 (let [i {:level 3 :code (:code record) :label (:desc record)}]
        (extend-extra i :cpa
                      (select-keys record
                                   [:includes :includes-2 :excludes])))))

(defn process-record [record]
  (when (#{1 4 6} (:level record))
    (let [i (process-category record)]
      (if (#{2 3} (:level i))
        (let [extend-with-cpc-dbg     (dbg :cpc extend-with-cpc)
              extend-with-prodcom-dbg (dbg :prodcom extend-with-prodcom)
              extend-with-cn-dbg      (dbg :cn extend-with-cn)
              extend-with-hs-dbg      (dbg :hs extend-with-hs)]
          ; Use the -dbg versions to see the output of each fn.
          (-> i
              extend-with-cpc-dbg
              extend-with-prodcom-dbg
              extend-with-cn;-dbg
              extend-with-hs;-dbg
              (merge {:syn [] :rel [] :exc []})))
        i))))

(defn get-sequence [records]
  (remove nil? (map process-record records)))

(defn nest [records]
  (binding [*out* *err*]
    (let [sequence (get-sequence records)]
      (reduce (fn [acc {:keys [level] :as item}]
                (case level
                  1 (conj acc (assoc item :items [])) ; TODO: dissoc :level

                                        ; acc[-1].items
                  2 (do
                      (update-in acc
                                 [(dec (count acc)) :items]
                                 #(conj % (assoc item :items []))))

                                        ; acc[-1].items[acc[-1].items.last.items]
                  3 (update-in acc
                               [(dec (count acc)) :items (dec (count (:items (last acc)))) :items]
                               #(conj % item))))
              [] sequence))))

(spit "public/workers/autocomplete/data.json"
      (json/generate-string (nest cpa-records) {:pretty true}))
