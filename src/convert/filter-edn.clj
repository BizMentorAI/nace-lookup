#!/usr/bin/env bb

(require '[clojure.pprint :refer [pprint]])
(require '[clojure.edn :as edn])

(def cpc-map-table (edn/read-string (slurp "src/data/cpa2cpc.edn")))
(def cpc-records (edn/read-string (slurp "src/data/cpc.edn")))

; The dict contains both hospitalisation and hospitalization, but only sg forms.
; Includes: (dict "word").
(def dict (into #{} (str/split-lines (slurp "src/data/nounlist.txt"))))

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

(defn get-nouns [text]
  ; TODO: Find plural forms as well (waters/ferries/cruises).
  (let [words (str/split (str/lower-case text) #"\s+")]
    (into #{} (filter #(dict %) words))))

; UNSPSC
; Match based on 3+ common nouns from:
; CPA title, CPC title, extra.
; Filter out non-nouns from the label.
(defn match-unspsc [record]
  (when (= (:level record) 3)
    (prn (:label record) (get-nouns (:label record)))
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
