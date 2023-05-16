#!/usr/bin/env bb

; filter-edn.clj src/data/cpa.edn > public/workers/autocomplete/data.json

(require '[clojure.pprint :refer [pprint]])
(require '[clojure.edn :as edn])

(defn exit-with-error [message]
  (throw (ex-info
          (str \u001b "[31m" "Error: " \u001b "[0m" message)
          {:babashka/exit 1})))

(when-not (= (count *command-line-args*) 1)
  (exit-with-error "only 1 argument (input file path) expected."))

(def input-file-path (first *command-line-args*))

; (str/join "." (take 2 (str/split (nth row 3) #"\.")))

; We can't use parent, because L2 is 14 and L1 C. There's no link, we cannot map that.
; We have to do it sequentially then.
(defn process-category [row]
  (case (second row)
    1 {:level 1 :label (nth row 4)}
    4 {:level 2 :code (nth row 2) :label (nth row 4)}
    6 {:level 3 :code (nth row 2) :label (nth row 4)
         :extra (str/trim (str (nth row 5) " " (nth row 6)))}))

  ;; (let [base {:level (Integer/parseInt (second row))
  ;;             :code (nth row 3) :label (nth row 4)}
  ;;       extra (str/trim (str (nth row 5) " " (nth row 6)))]
  ;;   (if (empty? extra) base (assoc base :extra extra)))

(defn process-row [row]
  (when (#{1 4 6} (second row)) (process-category row)))

; Remove empty (L1) L4 groups.
; (not (empty? (:items l4-item)))

; Then onclick modal.show()
(defn get-sequence [data]
  (remove nil? (map process-row data))
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

(let [input (edn/read-string (slurp input-file-path))]
  (println (json/generate-string
            (sequence-to-nested input)
            {:pretty true}
            ,)))
