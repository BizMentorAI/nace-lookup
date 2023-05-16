; This should have tests!
; The transducer should be in a CLJC file and we should include (inline!)
; it here.

(defn ^:async fetch-data []
  (let [response (js/await (js/fetch "/workers/autocomplete/data.json"))
        body (js/await (.json response))] body))

(def data (js->clj (js/await (fetch-data)) {:keywordize-keys true}))
;(js/console.log (clj->js data))

(defn- match-l6-item [item regexp]
  (let [fields [(:label item) (:extra item)]]
    (first (filter #(.match % regexp) fields))))

(defn data-transducer [search-term xf]
  (let [regexp (new js/RegExp (str "\\b" search-term) "i")]
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

      ([result] result))))

(defn- filter-items [data search-term]
  (persistent! (into [] (partial data-transducer search-term) data)))

(defn handle-message [event]
  (let [search-term event.data]
    (js/console.log "Worker received:" #js {:term search-term})
    (let [result (filter-items data search-term)]
      (js/console.log "Worker returned:" (clj->js result))
      (js/postMessage (clj->js result)))))

(set! onmessage handle-message)
