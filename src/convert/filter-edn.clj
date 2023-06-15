#!/usr/bin/env bb -cp src/convert

(require '[clojure.pprint :refer [pprint]])
(require '[clojure.edn :as edn])

(require '[inflections :refer (plural singular)])

(def cpa-records      (edn/read-string (slurp "src/data/cpa.edn")))
(def cpc-map-table    (edn/read-string (slurp "src/data/cpa2cpc.edn")))
(def cpc-records      (edn/read-string (slurp "src/data/cpc.edn")))
(def cn-map-table     (edn/read-string (slurp "src/data/cpa2cn.edn")))
(def hs-map-table     (edn/read-string (slurp "src/data/cpc212hs2017.edn")))
(def prodcom-records  (edn/read-string (slurp "src/data/prodcom2022-structure.edn")))
(def cn-title-records (edn/read-string (slurp "src/data/cn2023-titles.edn")))
(def cn-desc-records  (edn/read-string (slurp "src/data/cn2023.edn")))
(def hs-2017-records  (edn/read-string (slurp "src/data/hs-h4.edn")))

(defn get-cpc [{:keys [code]}]
  (let [map-record
        (first (filter #(= (:cpa-21-code %) code) cpc-map-table))
        cpc-record
        (first (filter #(= (:code %) (:cpc-21-code map-record)) cpc-records))]
    cpc-record))

(defn extend-meta [hash extra-meta]
  (with-meta hash (merge (or (meta hash) {}) extra-meta)))

(defn extend-extra [record extra-key value]
  (if (string? value)
    (if (not (empty? (str/trim value)))
      (assoc-in record [:extra extra-key] value)
      record)
    (let [values (filter (comp not empty?) value)]
      (if (not (empty? values))
        (assoc-in record [:extra extra-key] (str/join " " values))
        record))))

;; (defn normalise [text]
;;   (-> text
;;       (str/replace #"\(excluding .*\)", "")
;;       (str/replace #"[()]" "")
;;       (str/replace #"\"" "")
;;       (str/replace #"n.e.c." "")
;;       (str/replace #"[\d,.]+" "")
;;       (str/replace #"'s?" "")
;;       (str/replace #"etc.?" "")
;;       (str/replace #"</?\w+>" "")
;;       (str/replace #"(?i)dry clean" "dry-clean")
;;       (str/replace #"(?i)(cow|chick|pigeon) pea" "$1pea")
;;       (str/replace #"\b(except|excluding|without).+$" "")))

(defn normalise [text]
  (-> (str/lower-case text)
      (str/replace #"</?\w+>" "")
      (str/replace #"[-:,*()\[\]\"\.]" "")
      (str/replace #"\d+" "")
      (str/replace #"\b(and|or|this|subcategory|of|does|not|includes|cf|other|the|an?|nec)\b" "")
      (str/replace #"\(s\)" "")
      (str/replace #"\b\((except|excluding|without)[^)]\)" "")
      (str/replace #"\bexcl\b.+$" "") ; FIXME
      (str/replace #"\s+" " ")
      (str/trim)))

(defn normalise-fields [record fields]
  (normalise (str/trim (str/join " " (map #(% record) fields)))))

(defn extend-with-cpc [record]
  (if-let [cpc-record (get-cpc record)]
    (-> record
        (extend-meta {:cpc-record cpc-record})
        (extend-extra :cpc (normalise-fields cpc-record [:title :note])))
    record))

(defn get-prodcom [{:keys [:code]}]
  (filter #(= (:cpa %) code) prodcom-records))

(defn extend-with-prodcom [record]
  (if-let [prodcom-records (get-prodcom record)]
    (do
      (-> record
          (extend-meta {:prodcom-records prodcom-records})
          (extend-extra :prodcom
                        (normalise
                         (str/join "\n" (map :en prodcom-records))))))
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
    (-> record
        (extend-extra :cn-title
                      (normalise
                       (str/join "\n" (map :dm selected-cn-title-records))))

        (extend-extra :cn-desc
                      (normalise
                       (str/join "\n" (map :desc selected-cn-desc-records)))))))

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
    (extend-meta record {:hs-2017-records selected-hs-2017-records})
    (when (not (empty? selected-hs-2017-records))
      (extend-extra record :hs
                    (normalise
                     (str/join "\n"
                               (into #{}
                                     (map (comp str/trim :desc)
                                          selected-hs-2017-records))))))))

(def generic-keywords
  (into #{}
        (flatten
         (map #(vec [%, (plural %)])
              ["and" "or" "at" "in" "on" "about" "of" "as" "for" "the"
               "a" "an" "≤" "including" "like" "by" "with" "not" "any"
               "whether" "similar" "genus" "spp." "purpose" "used" "to"
               "primarily" "from" "kg" "mm" "cm" "km" "m" ">" "<" "v"
               "W" "kvar" "/" "can" "only" "use" "other" "it" "simply"
               "further" "but" "their" "organisation"]))))

(defn tokenise [text]
  (str/split text #"[\s,;:]+"))

(defn process-category [record]
  (case (:level record)
    1 {:level 1 :label (:desc record)}
    4 {:level 2 :code (:code record) :label (:desc record)}
    6 (let [i {:level 3 :code (:code record) :label (:desc record)}]
        (extend-extra i :cpa (normalise-fields record [:includes :includes])))))

(defn process-record [record]
  (when (#{1 4 6} (:level record))
    (let [i (process-category record)]
      (if (= (:level i) 3)
        (-> i extend-with-cpc extend-with-prodcom extend-with-cn extend-with-hs)
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
