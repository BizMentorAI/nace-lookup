(ns workers.autocomplete.transducer
  "Usage: (persistent! (into [] (partial transducer search-term) data))")

(defn- match-l6-item [item regexp]
  (let [fields [(:label item) (:extra item)]]
    (first (filter #(re-find regexp %) fields))))

; A transducer (sometimes referred to as xform or xf)
; is a transformation from one reducing function to another.

; So both conj as well as the inner function in the transducer are rfs.

; also helps explain the behavior of manually composing them like (reduce ((map inc) +) 0 (range 4)) . at first that stuff looked so strange to me. But remembering that a transducer (map inc) is a function that takes a reducing function and returns a new reducing function it makes it very clear to read (edited)

; Another confusing thing is that functions like cat are transducers, where functions like map return transducers, but it's common to refer to both as transducers.

; https://clojurians.slack.com/archives/C053AK3F9/p1684356127347079
; https://clojurians.slack.com/archives/C053AK3F9/p1684357926218779

;; (defn my-transducer [search-term]
;;   (let [regexp (re-pattern (str "(?i)\\b" search-term))]
;;     (comp (mapcat :items)
;;           (filter (fn [{:keys [l6-item]}]
;;                     (match-l6-item l6-item regexp)))
;;           (map (fn [l6-item]
;;                  (assoc l6-item
;;                         :l4Item
;;                         {:code (:code l4-item) :label (:label l4-item)}))))))

;; ;; usage
;; (into [] (my-transducer search-term) coll)

(defn transducer [search-term rf]
  (let [regexp (re-pattern (str "(?i)\\b" search-term))]
    (fn
      ([] (rf))

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
           (rf acc (dissoc l1-item :items))
           (doseq [l6-item filtered-l6-items]
             (rf acc l6-item)))) ; <--- doseq won't return this, so this might be useless.
       acc)

                                        ; TODO: Put persistent here? Will it work in bb/JVM?
      ([result] result))))
