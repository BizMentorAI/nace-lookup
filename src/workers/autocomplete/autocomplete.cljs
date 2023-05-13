(defn ^:async fetch-data []
  (let [response (js/await (js/fetch "/workers/autocomplete/data.json"))
        body (js/await (.json response))]
    body))

(def data (js/await (fetch-data)))

(defn- match-l6-item [fields search-term]
  ; TODO: ^search-term
  (js/console.log "match-l6-item")
  (first (filter (fn [field] (re-find search-term field)) fields)))

(defn- match-l6-items [items search-term]
  (js/console.log "match-l6-items")
  (map (fn [^:js {:keys [code label]}]  ; TODO: extra
         (when (match-l6-item [label])  ; TODO: extra
           {:code code :label label})) items))

(defn- item-matches [^:js {:keys [code label items]} search-term]
  (js/console.log "item-matches")
  (let [matched-items (match-l6-items items search-term)]
    (when-not (empty? matched-items)
      {:code code :label :label :items matched-items})))

(defn- filter-items [data search-term]
  (js/console.log "filter-items")
  (reduce (fn [acc l4-item]
            (let [match-result (item-matches l4-item search-term)]
              (if match-result) (conj acc match-result) match-result)) {} data))

(defn handle-message [event]
  (let [search-term event.data]
    (js/console.log "Worker received:" #js {:term search-term})
    (let [result (filter-items data search-term)]
      (js/postMessage (clj->js result)))))

(set! js/self.onmessage handle-message)
