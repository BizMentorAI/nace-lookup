(defn ^:async fetch-data []
  (let [response (js/await (js/fetch "/workers/autocomplete/data.json"))
        body (js/await (.json response))]
    body))

(def data (js/await (fetch-data)))
;(js/console.log data)

(defn- match-l6-item [fields regexp]
  (first (filter #(.match % regexp) fields)))

(defn data-transducer [search-term xf]
  (let [regexp (new js/RegExp (str "\\b" search-term) "i")]
    (fn
      ([] (xf))

      ([acc input]
       (doseq [l4-item (:items input)]
         (xf acc l4-item)
         (doseq [l6-item (:items l4-item)]
           (when (match-l6-item [(:label l6-item) (:extra l6-item)] regexp)
             (xf acc l6-item))))
       acc)

      ([result] result))))

(defn- filter-items [data search-term]
  (into []
        (partial data-transducer search-term)
        (js->clj data {:keywordize-keys true})))

(defn handle-message [event]
  (let [search-term event.data]
    (js/console.log "Worker received:" #js {:term search-term})
    (let [result (filter-items data search-term)]
      (js/console.log "CLJ" result)
      (js/console.log "JS" (clj->js result))
      (js/postMessage result))))

(set! onmessage handle-message)
