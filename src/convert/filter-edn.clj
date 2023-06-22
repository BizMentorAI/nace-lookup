#!/usr/bin/env bb

(require '[clojure.pprint :refer [pprint]])
(require '[clojure.edn :as edn])
(require '[clojure.set :as set])

(def cpa-records (edn/read-string (slurp "src/data/cpa21.edn")))

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
    (cond (map? value) :map (coll? value) :coll true :error)))

(defn- clean-value [value]
  (if (coll? value)
    (str/join "\n" (map str/trim (remove #{"Other"} (distinct value))))
    value))

(defn- clean-pairs [hash]
  (remove (fn [[_ v]] (empty? v)) hash))

(defn- clean-map-from-pairs [list]
  (into {} (map (fn [[k v]] [k (clean-value v)]) list)))

(defn clean-map [hash]
  (clean-map-from-pairs (clean-pairs hash)))

(defmethod extend-extra :map [record extra-key value]
  (let [result (clean-map value)]
    (if (not (empty? result))
      (assoc-in record [:extra extra-key] result)
      record)))

(defmethod extend-extra :coll [record extra-key value]
  (let [result (map clean-map value)]
    (if (not (empty? result))
      (assoc-in record [:extra extra-key] (vec result))
      record)))

; CPC
(def cpc-map-table (edn/read-string (slurp "src/data/cpa21-to-cpc21.edn")))
(def cpc-records   (edn/read-string (slurp "src/data/cpc21.edn")))

(defn get-cpc [{:keys [code]}]
  (let [map-records (filter #(= (:cpa-21-code %) code) cpc-map-table)]
    (flatten
     (map (fn [map-record]
            (filter #(= (:code %) (:cpc-21-code map-record)) cpc-records))
          map-records))))

; NOTE: The map table seems to contain only L6 items,
; but that's probably covered by PRODCOM.
(defn extend-with-cpc [record]
  (let [cpc-records (get-cpc record)]
    (-> record
        (extend-meta {:cpc-records cpc-records})
        (extend-extra :cpc (map
                            #(select-keys % [:code :title :note])
                            cpc-records)))))

; PRODCOM
(def prodcom-records (edn/read-string (slurp "src/data/prodcom2022-structure.edn")))

; Get product sub-categories for L6 items (under L6)
; which is what the PRODCOM specification provides.
(defn get-prodcom [{:keys [code]}]
  (filter #(= (:cpa %) code) prodcom-records))

(defn extend-with-prodcom [record]
  (let [prodcom-records (get-prodcom record)]
    (-> record
        (extend-meta {:prodcom-records prodcom-records})
        (extend-extra :prodcom (distinct
                                (map
                                 #(select-keys % [:code :title])
                                 prodcom-records))))))

; CN
(def cn-map-table     (edn/read-string (slurp "src/data/cpa21-to-cn2023.edn")))
(def cn-title-records (edn/read-string (slurp "src/data/cn2023-titles.edn")))
(def cn-desc-records  (edn/read-string (slurp "src/data/cn2023.edn")))

(defn extend-with-cn [{:keys [code] :as record}]
  (let [map-items (filter #(= (:cpa %) code) cn-map-table)

        selected-cn-records
        (reduce (fn [acc map-item]
                  (let [selected-title-records
                        (filter #(= (map-item :cn) (:cn %)) cn-title-records)

                        selected-desc-records
                        (filter #(= (map-item :cn) (% :code)) cn-desc-records)]

                    (conj acc
                          {:code (:cn map-item)
                           :title (str/join "\n" (map :dm selected-title-records))
                           :desc (str/join "\n" (map :desc selected-desc-records))})))
                [] map-items)]
    (extend-extra record :cn selected-cn-records)))

; HS
(def hs-map-table   (edn/read-string (slurp "src/data/cpc21-to-hs2017.edn")))
(def hs2017-records (edn/read-string (slurp "src/data/hs-h4.edn")))
; The mapping is guessed, but it just might be alright.
; TODO: Once CPC/HS2022 mapping is out, use it.
(def hs2022-records (map #(set/rename-keys % {:id :code, :text :desc})
                          (edn/read-string (slurp "src/data/hs2022.edn"))))

(defn extend-with-hs [record key-label all-records]
  ;(when (= (:level record) 2) (prn record)) ; Any extra?
  (let [cpc-codes (into #{} (map :code (get-in record [:extra :cpc])))
        map-records (filter #(cpc-codes (:cpc-21 %)) hs-map-table)

        selected-records
        (flatten (map (fn [map-record]
                        (filter #(= (str/replace (:hs-2017 map-record) #"\." "")
                                    (:code %))
                                all-records))
                      map-records))]
    (if (not (empty? selected-records))
      (-> record
          (extend-meta {(name (str key-label "-records")) selected-records})
          (extend-extra key-label (map
                                   #(select-keys % [:code :desc])
                                   selected-records)))
      record)))

(defn extend-with-hs2017 [record]
  (extend-with-hs record :hs2017 hs2017-records))

(defn extend-with-hs2022 [record]
  (extend-with-hs record :hs2022 hs2022-records))

(defn process-category [record]
  (if (= (:level record) 1)
    {:level 1 :label (:desc record)}
    (let [i {:level (/ (:level record) 2)
             :code (:code record)
             :label (:desc record)}]
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
              extend-with-hs2017-dbg  (dbg :hs extend-with-hs)
              extend-with-hs2022-dbg  (dbg :hs extend-with-hs)]
          ; Use the -dbg versions to see the output of each fn.
          (-> i
              extend-with-cpc;-dbg
              extend-with-prodcom;-dbg
              extend-with-cn;-dbg
              extend-with-hs2017;-dbg
              extend-with-hs2022;-dbg
              (merge {:syn [] :rel [] :exc []})))
        i))))

(defn get-sequence [records]
  (remove nil? (map process-record records)))

(defn nest [records]
  (binding [*out* *err*]
    (let [sequence (get-sequence records)]
      (reduce (fn [acc {:keys [level] :as item}]
                (case level
                  1 (conj acc (assoc (dissoc item :level) :items []))

                                        ; acc[-1].items
                  2 (do
                      (update-in acc
                                 [(dec (count acc)) :items]
                                 #(conj % (assoc (dissoc item :level) :items []))))

                                        ; acc[-1].items[acc[-1].items.last.items]
                  3 (update-in acc
                               [(dec (count acc)) :items (dec (count (:items (last acc)))) :items]
                               #(conj % (dissoc item :level)))))
              [] sequence))))

(spit "public/workers/autocomplete/data.edn"
      (with-out-str (pprint (vec (nest cpa-records)))))

;; (spit "public/workers/autocomplete/data.json"
;;       (json/generate-string (nest cpa-records) {:pretty true}))
