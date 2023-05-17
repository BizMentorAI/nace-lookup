(ns workers.autocomplete.transducer
  "Usage: (persistent! (into [] (partial transducer search-term) data))")

(defn- match-l6-item [item regexp]
  (let [fields [(:label item) (:extra item)]]
    (first (filter #(re-find regexp %) fields))))

(defn transducer [search-term xf]
  (let [regexp (re-pattern (str "(?i)\\b" search-term))]
    (fn
      ([] (xf))

      ([acc l1-item]
       (let [filtered-l6-items
             (reduce
              (fn [acc l4-item]
                (let [filtered-l6-items
                      (filter
                       (fn [l6-item] (match-l6-item l6-item regexp))
                       (:items l4-item))

                      extended-l6-items
                      (map (fn [l6-item]
                             (assoc l6-item
                                    :l4Item
                                    {:code (:code l4-item) :label (:label l4-item)}))
                           filtered-l6-items)]
                  (if-not (empty? extended-l6-items)
                    (apply conj acc extended-l6-items)
                    acc)))
              []
              (:items l1-item))]

         (when-not (empty? filtered-l6-items)
           (xf acc (dissoc l1-item :items))
           (doseq [l6-item filtered-l6-items]
             (xf acc l6-item))))
       acc)

      ; TODO: Put persistent here? Will it work in bb/JVM?
      ([result] result))))
