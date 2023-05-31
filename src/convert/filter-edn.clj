#!/usr/bin/env bb

; filter-edn.clj src/data/cpa.edn > public/workers/autocomplete/data.json

(require '[clojure.pprint :refer [pprint]])
(require '[clojure.edn :as edn])

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
  (when (#{1 4 6} (:level record)) (process-category record)))

; Remove empty (L1) L4 groups.
; (not (empty? (:items l4-item)))

; Then onclick modal.show()
(defn get-sequence [data]
  (remove nil? (map process-record data))
  ;; (reduce (fn [acc item]
  ;;           ;; (binding [*out* *err*] (prn item))
  ;;           (if (= (:level item) level)
  ;;             (conj acc (assoc (dissoc item :level) :items []))
  ;;             (conj (butlast acc)
  ;;                   (update-in (last acc) [:items] #(conj % (dissoc item :level))))))
  ;;         []
  ;;         (filter identity (map process-row data)))
  )

(defn sequence-to-nested [data]
  (binding [*out* *err*]
    (let [sequence (get-sequence data)]
      (reduce (fn [acc {:keys [level] :as item}]
                (case level
                  1 (conj acc (assoc item :items [])) ; TODO: dissoc :level

                  ; acc[-1].items
                  2 (do
                      (update-in acc [(dec (count acc)) :items]
                                 #(conj % (assoc item :items []))))

                  ; acc[-1].items[acc[-1].items.last.items]
                  3 (update-in acc [(dec (count acc)) :items (dec (count (:items (last acc)))) :items]
                               #(conj % item))))
              [] sequence))))

(let [records (edn/read-string (slurp "src/data/cpa.edn"))]
  (spit "public/workers/autocomplete/data.json"
        (json/generate-string
         (sequence-to-nested records)
         {:pretty true})))
