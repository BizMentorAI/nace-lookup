#!/usr/bin/env bb -cp src/convert

(require '[clojure.pprint :refer [pprint]])
(require '[clojure.edn :as edn])

(require '[inflections :refer (plural singular)])

(def cpa-records (edn/read-string (slurp "src/data/cpa.edn")))
(def cpc-map-table (edn/read-string (slurp "src/data/cpa2cpc.edn")))
(def cpc-records (edn/read-string (slurp "src/data/cpc.edn")))
(def cpc-isic-map-table (edn/read-string (slurp "src/data/cpc2isic.edn")))
(def isic-naics-map-table (edn/read-string (slurp "src/data/isic2naics.edn")))
(def naics-records (edn/read-string (slurp "src/data/naics.edn")))
;; (def unspsc-L6-records (filter #(not (= (mod (:id %) 10) 0))
;;                                (edn/read-string (slurp "src/data/unspsc.edn"))))

; Extra: remove HTML, remove \n, make hash :extra {:cpa "..." :cpc "..." :unspsc "..."}
; CPC
(defn get-cpc [{:keys [:code]}]
  (let [map-record
        (first (filter #(= (:cpa-21-code %) code) cpc-map-table))
        cpc-record
        (first (filter #(= (:code %) (:cpc-21-code map-record)) cpc-records))]
    cpc-record))

(defn extend-meta [hash extra-meta]
  (with-meta hash (merge (meta hash) extra-meta)))

(defn extend-with-cpc [record]
  (if-let [cpc-record (get-cpc record)]
    (-> record
        (extend-meta {:cpc-record cpc-record})
        (assoc :cpc (:code cpc-record))
        (assoc-in [:extra :cpc]
                  (str/trim
                   (str (:title cpc-record) " " (:note cpc-record)))))
    record))

(defn extend-with-isic-code [record]
  (if-let [cpc-record (:cpc-record (meta record))]
    (let [cpc-isic-map-record
          (first
           (filter #(= (:cpc-21-code %) (:code cpc-record))
                   cpc-isic-map-table))]
      (extend-meta record {:isic-code (:isic-4-code cpc-isic-map-record)}))
    record))

(defn extend-with-naics-code [record]
  (if-let [isic-code (:isic-code (meta record))]
    (let [f (fn [i] (= (:isic-40 i) isic-code))
          isic-map-record (first (filter f isic-naics-map-table))]
      (extend-meta record {:naics-code
                           (:2022-naics-us isic-map-record)}))
    record))

; TODO extend with ISIC desc as well
(defn extend-with-naics-extra [record]
  (if-let [naics-code (:naics-code (meta record))]
    (let [naics-record
          (first (filter #(= (:id %) naics-code) naics-records))]
      (-> record
          (extend-meta {:naics-record naics-record})
          (assoc-in [:extra :naics]
                    (str/trim
                     (str (:title naics-record) " " (:description naics-record))))))
    record))

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
       :extra {:cpa (str/trim (str (:includes record) " "
                                   (:includes-2 record)))}}))

  ;; (let [base {:level (Integer/parseInt (second row))
  ;;             :code (nth row 3) :label (nth row 4)}
  ;;       extra (str/trim (str (nth row 5) " " (nth row 6)))]
  ;;   (if (empty? extra) base (assoc base :extra extra)))

(defn process-record [record]
  (when (#{1 4 6} (:level record))
    (-> (process-category record)
        extend-with-cpc
        extend-with-isic-code
        extend-with-naics-code
        extend-with-naics-extra)))

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
                ;; (prn [acc level item])
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
