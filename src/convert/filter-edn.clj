#!/usr/bin/env bb -cp src/convert

(require '[clojure.pprint :refer [pprint]])
(require '[clojure.edn :as edn])

(require '[inflections :refer (plural singular)])

(def cpa-records (edn/read-string (slurp "src/data/cpa.edn")))
(def cpc-map-table (edn/read-string (slurp "src/data/cpa2cpc.edn")))
(def cpc-records (edn/read-string (slurp "src/data/cpc.edn")))
;; (def cpc-isic-map-table (edn/read-string (slurp "src/data/cpc2isic.edn")))
;; (def isic-naics-map-table (edn/read-string (slurp "src/data/isic2naics.edn")))
(def cn-map-table (edn/read-string (slurp "src/data/cpa2cn.edn")))
(def hs-map-table (edn/read-string (slurp "src/data/cpc212hs2017.edn")))
;; (def naics-records (edn/read-string (slurp "src/data/naics-index.edn")))
(def prodcom-records (edn/read-string (slurp "src/data/prodcom2022-structure.edn")))
(def cn-records (edn/read-string (slurp "src/data/cn2023.edn")))
(def hs-records (edn/read-string (slurp "src/data/hs-h4.edn")))
;; (def unspsc-L6-records (filter #(not (= (mod (:id %) 10) 0))
;;                                (edn/read-string (slurp "src/data/unspsc.edn"))))

(defn get-cpc [{:keys [:code]}]
  (let [map-record
        (first (filter #(= (:cpa-21-code %) code) cpc-map-table))
        cpc-record
        (first (filter #(= (:code %) (:cpc-21-code map-record)) cpc-records))]
    cpc-record))

(defn extend-meta [hash extra-meta]
  (with-meta hash (merge (meta hash) extra-meta)))

(defn normalise-2 [text]
  (-> (str/lower-case text)
      (str/replace #"</?\w+>" "")
      (str/replace #"[-:,*()\.]" "")
      (str/replace #"\d+" "")
      (str/replace #"\b(and|or|this|subcategory|of|does|not|includes|cf|other|the|an?|nec)\b" "")
      (str/replace #"\(s\)" "")
      (str/replace #"\b\((except|excluding|without)[^)]\)" "")
      (str/replace #"\s+" " ")
      (str/trim)))

(defn normalise-fields [record fields]
  (normalise-2 (str/trim (str/join " " (map #(% record) fields)))))

(defn extend-with-cpc [record]
  (if-let [cpc-record (get-cpc record)]
    (-> record
        (extend-meta {:cpc-record cpc-record})
        ;; (assoc :cpc (:code cpc-record))
        (assoc-in [:extra :cpc]
                  (normalise-fields cpc-record [:title :note])))
    record))

(defn get-prodcom [{:keys [:code]}]
  (filter #(= (:cpa %) code) prodcom-records))

(defn extend-with-prodcom [record]
  (if-let [prodcom-records (get-prodcom record)]
    (do
      (-> record
          (extend-meta {:prodcom-records prodcom-records})
          (assoc-in [:extra :prodcom]
                    (str/join " "
                              (into #{}
                                    (str/split
                                     (normalise-2
                                      (str/join "\n"
                                                (map :en prodcom-records)))
                                     #"\s+"))))))
    record))

(defn extend-with-cn [{:keys [code] :as record}]
  (let [map-items (filter #(= (:cpa %) code) cn-map-table)
        cn-records (flatten
                    (map
                     (fn [map-item]
                       (filter #(= (map-item :cn) (% :code)) cn-records))
                     map-items))]
    (-> record
        (assoc-in [:extra :cn]
                  (str/join " "
                            (into #{}
                                  (str/split
                                   (normalise-2
                                    (str/join "\n"
                                              (map :desc cn-records)))
                                   #"\s+")))))))

(defn extend-with-hs [{:keys [code] :as record}]
  record)

;; (defn extend-with-isic-code [record]
;;   (if-let [cpc-record (:cpc-record (meta record))]
;;     (let [cpc-isic-map-record
;;           (first
;;            (filter #(= (:cpc-21-code %) (:code cpc-record))
;;                    cpc-isic-map-table))]
;;       (extend-meta record {:isic-code (:isic-4-code cpc-isic-map-record)}))
;;     record))

;; (defn extend-with-naics-code [record]
;;   (if-let [isic-code (:isic-code (meta record))]
;;     (let [f (fn [i] (= (:isic-40 i) isic-code))
;;           naics-record (first (filter f isic-naics-map-table))]
;;     (prn naics-record)
;;       (extend-meta record {:naics-code
;;                            (:2022-naics-us naics-record)}))
;;     record))

;; ; TODO extend with ISIC desc as well
;; (defn extend-with-naics-extra [record]
;;   (if-let [naics-code (:naics-code (meta record))]
;;     (let [matched-naics-records
;;           (filter #(= (:code %) naics-code) naics-records)
;;           matched-descs (map :desc matched-naics-records)]
;;       (-> record
;;           (extend-meta {:naics-records matched-naics-records})
;;           (assoc-in [:extra :naics]
;;                     (normalise-2
;;                      (str/join " "
;;                                (into #{}
;;                                      (str/split
;;                                       (str/join " " matched-descs)
;;                                       #"\s+")))))))
;;     record))

(defn acronym? [word]
  (re-find #"^[A-Z0-9-]+$" word))

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

; Here the lib fucks up.
(def dont-singularise
  #{"miscellaneous" "religious" "alliaceous" "tuberous" "citrus" "gas"
    "precious" "semi-precious" "leguminous" "asparagus" "oleaginous"
    "apparatus" "coniferous" "non-coniferous" "bituminous" "gaseous"
    "non-ferrous" "ferrous" "calcareous"})

(def pl-sg-map
  {"sloes" "sloe", "olives" "olive", "leaves" "leaf", "cloves" "clove",
   "valves" "valve", "nurseries" "nursery", "roes" "roe"})

(defn sg [word]
  (or (pl-sg-map word) (singular word)))

(defn singular-fix [word]
  (if (dont-singularise word) word (sg word)))

(defn tokenise [text]
  (str/split text #"[\s,;:]+"))

(defn get-keywords [text]
  (let [words (tokenise text)]
    (into #{}
          (map
           ; Leave acronyms as is, lower-case other words.
           #(if (acronym? %) % (singular-fix (str/lower-case %)))

           ; Filter out generic words.
           (filter #(not (generic-keywords (str/lower-case %))) words)))))

(defn normalise [text]
  (-> text
      (str/replace #"\(excluding .*\)", "")
      (str/replace #"[()]" "")
      (str/replace #"\"" "")
      (str/replace #"n.e.c." "")
      (str/replace #"[\d,.]+" "")
      (str/replace #"'s?" "")
      (str/replace #"etc.?" "")
      (str/replace #"</?\w+>" "")
      (str/replace #"(?i)dry clean" "dry-clean")
      (str/replace #"(?i)(cow|chick|pigeon) pea" "$1pea")
      (str/replace #"\b(except|excluding|without).+$" "")))

; Extra: remove HTML.
;; ; UNSPSC
;; (defn match-unspsc-keywords [keywords]
;;   (reduce (fn [acc unspsc-item]
;;             (let [a (into #{} (get-keywords (normalise (or (:desc unspsc-item) ""))))
;;                   matched-keywords (clojure.set/intersection a keywords)]
;;               (if (not (empty? matched-keywords))
;;                 (conj acc {:record unspsc-item :matched-keywords matched-keywords})
;;                 acc)))
;;           []
;;           unspsc-L6-records))

;; (defn get-best-match [matches]
;;   (last (sort-by #(count (:matched-keywords %)) matches)))

;; ; TODO: multi-thread.
;; ; Total Number of Cores:	8 (4 performance and 4 efficiency)
;; ; Run on JVM (benchmark against bb).
;; (defn match-unspsc [record]
;;   (when (= (:level record) 3)
;;     (let [cpc-record (:record (meta cpc-record))
;;           cpc-title (:title cpc-record)
;;           keywords (get-keywords (normalise (str (:label record) " " cpc-title)))]
;;       ;; (prn :cpa (:label record) :cpc cpc-title :keywords keywords)
;;       (let [matches (match-unspsc-keywords keywords)
;;             best-match (get-best-match matches)]
;;         (prn record (count matches)) ;;;
;;         (:record best-match)))))

;; (defn extend-with-unspsc [record]
;;   (or (if-let [unspsc-record (match-unspsc record)]
;;         (-> record
;;             ;; (assoc :unspsc (with-meta
;;             ;;                  (:code unspsc-record)
;;             ;;                  {:record unspsc-record}))
;;             (assoc :extra (str/trim (str (:extra record) " "
;;                                          (:title unspsc-record) " "
;;                                          (:desc unspsc-record))))))
;;       record))

(defn process-category [record]
  (case (:level record)
    1 {:level 1 :label (:desc record)}
    4 {:level 2 :code (:code record) :label (:desc record)}
    6 {:level 3 :code (:code record) :label (:desc record)
       :extra {:cpa (normalise-fields record [:includes :includes-2])}}))

  ;; (let [base {:level (Integer/parseInt (second row))
  ;;             :code (nth row 3) :label (nth row 4)}
  ;;       extra (str/trim (str (nth row 5) " " (nth row 6)))]
  ;;   (if (empty? extra) base (assoc base :extra extra)))

(defn process-record [record]
  (when (#{1 4 6} (:level record))
    (let [i (process-category record)]
      (if (= (:level i) 3)
        (-> i
            extend-with-cpc
            extend-with-prodcom
            extend-with-cn
            extend-with-hs)
        i))))

; Remove empty (L1) L4 groups.
; (not (empty? (:items l4-item)))

(defn get-sequence [records]
  (remove nil? (map process-record records))
  ;; (reduce (fn [acc item]
  ;;           ;; (binding [*out* *err*] (prn item))
  ;;           (if (= (:level item) level)
  ;;             (conj acc (assoc (dissoc item :level) :items []))
  ;;             (conj (butlast acc)
  ;;                   (update-in (last acc) [:items] #(conj % (dissoc item :level))))))
  ;;         []
  ;;         (filter identity (map process-row data)))
  )

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
