#!/usr/bin/env bb

(require '[clojure.pprint :refer [pprint]])
(require '[clojure.edn :as edn])

(defn exit-with-error [message]
  (throw (ex-info
          (str \u001b "[31m" "Error: " \u001b "[0m" message)
          {:babashka/exit 1})))

(when-not (= (count *command-line-args*) 1)
  (exit-with-error "only 1 argument (input file path) expected."))

(def input-file-path (first *command-line-args*))

(defn process-category [row]
  (case (second row)
    "1" {:level 1 :parent :top-level  :label (nth row 4)}
    "4" {:level 2 :parent (nth row 3) :code (nth row 2) :label (nth row 4)}
    "6" {:level 3 :parent (nth row 3) :code (nth row 2) :label (nth row 4)
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
  (get-sequence data)
  ;; (binding [*out* *err*]
  ;;   (let [my-sequence (get-sequence data)
  ;;         leaf-nodes (filter #(= (:level %) 3) my-sequence)
  ;;         grouped-leaf-nodes (group-by :parent leaf-nodes)

  ;;         l4-items (map (fn [[parent-code items]]
  ;;                         (let [parent (first (filter #(= (:code %) parent-code) my-sequence))]
  ;;                           (when-not parent (throw (ex-info "No parent" {:parent-code parent-code})))
  ;;                           (assoc parent :items items)))
  ;;                       grouped-leaf-nodes)
  ;;         ]
  ;;     l4-items
  ;;                                       ;(pprint (group-by :parent my-sequence))
  ;;     ,))
  )

(let [input (edn/read-string (slurp input-file-path))]
  (println (json/generate-string
            (sequence-to-nested input)
            {:pretty true})))
