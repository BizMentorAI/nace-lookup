#!/usr/bin/env bb -cp src/convert

(require '[clojure.pprint :refer [pprint]])
(require '[clojure.edn :as edn])

(require '[inflections :refer (plural singular)])

(def cpc-map-table (edn/read-string (slurp "src/data/cpa2cpc.edn")))
(def cpc-records (edn/read-string (slurp "src/data/cpc.edn")))

; Add unknown things into nounlist.txt and rebuild the dict.
;; (def dict (edn/read-string (slurp "src/data/dict.edn")))

; CPC
(defn get-cpc [{:keys [:code]}]
  (let [map-record
        (first (filter #(= (:cpa-21-code %) code) cpc-map-table))
        cpc-record
        (first (filter #(= (:code %) (:cpc-21-code map-record)) cpc-records))]
    cpc-record))

(defn extend-with-cpc [record]
  (or (if-let [cpc-record (get-cpc record)]
        (-> record
            (assoc :cpc (:code cpc-record))
            (assoc :extra (str/trim (str (:extra record) " "
                                         (:title cpc-record) " "
                                         (:note cpc-record))))))
      record))

(defn acronym? [word]
  (re-find #"^[A-Z0-9-]+$" word))

;; (defn noun? [word]
;;   (dict (str/lower-case word)))

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

; TODO: Get CPC titles too.
(defn get-keywords [text]
  (let [words (str/split text #"[\s,;:]+")]
    (into #{}
          (map
           ; Leave acronyms as is, lower-case other words.
           #(if (acronym? %) % (singular-fix (str/lower-case %)))

           (->> words
                ; Filter out generic keywords.
                (filter #(not (generic-keywords (str/lower-case %))))

                ; Only take acronyms or nouns.
                ;(filter #(or (acronym? %) (noun? %)))
                )))))

(defn get-label [record]
  (-> (:label record)
      (str/replace #"\(excluding .*\)", "")
      (str/replace #"[()]" "")
      (str/replace #"\"" "")
      (str/replace #"n.e.c." "")
      (str/replace #"[\d,.]+" "")
      (str/replace #"'s?" "")
      (str/replace #"etc.?" "")
      (str/replace #"(?i)dry clean" "dry-clean")
      (str/replace #"(?i)(cow|chick|pigeon) pea" "$1pea")
      (str/replace #"\b(except|excluding|without).+$" "")))

; UNSPSC
; Match based on 3+ common nouns from:
; CPA title, CPC title, extra.
; Filter out non-nouns from the label.
(defn match-unspsc [record]
  (when (= (:level record) 3)
    (prn (:label record) (get-keywords (get-label record)))
    ,))

(defn extend-with-unspsc [record]
  (or (if-let [cpc-record (match-unspsc record)]
        (-> record
            (assoc :cpc (:code cpc-record))
            (assoc :extra (str/trim (str (:extra record) " "
                                         (:title cpc-record) " "
                                         (:note cpc-record))))))
      record))

(defn process-category [record]
  (case (:level record)
    1 {:level 1 :label (:desc record)}
    4 {:level 2 :code (:code record) :label (:desc record)}
    6 {:level 3 :code (:code record) :label (:desc record)
       :extra (str/trim (str (:includes record) " "
                             (:includes-2 record)))}))

  ;; (let [base {:level (Integer/parseInt (second row))
  ;;             :code (nth row 3) :label (nth row 4)}
  ;;       extra (str/trim (str (nth row 5) " " (nth row 6)))]
  ;;   (if (empty? extra) base (assoc base :extra extra)))

(defn process-record [record]
  (when (#{1 4 6} (:level record))
    (-> (process-category record)
        extend-with-cpc
        extend-with-unspsc)))

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

(let [records (edn/read-string (slurp "src/data/cpa.edn"))]
  (spit "public/workers/autocomplete/data.json"
        (json/generate-string (nest records) {:pretty true})))
