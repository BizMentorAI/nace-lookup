(defn ^:async fetch-data []
  (let [response (js/await (js/fetch "/workers/autocomplete/data.json"))
        body (js/await (.json response))] body))

(def data (js->clj (js/await (fetch-data)) {:keywordize-keys true}))
;(js/console.log (clj->js data))

(defn- match-l6-item [fields regexp]
  (first (filter #(.match % regexp) fields)))

(defn data-transducer [search-term xf]
  (let [regexp (new js/RegExp (str "\\b" search-term) "i")]
    (fn
      ([] (xf))

      ([acc l1-item]
       (doseq [l4-item (:items l1-item)]
         (let [filtered-l6-items
               (filter (fn [l6-item]
                         (when (match-l6-item [(:label l6-item) (:extra l6-item)] regexp)
                           (xf acc l6-item)))
                       (:items l4-item))]
           (when-not (empty? filtered-l6-items)
             (xf acc (dissoc l1-item :items)) ; This needs raising to L1 level
             (doseq [l6-item filtered-l6-items]
               (xf acc (assoc l6-item :l4 (l4-item :label)))))))
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
