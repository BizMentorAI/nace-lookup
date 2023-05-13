(defn ^:async fetch-data []
  (let [response (js/await (js/fetch "/workers/autocomplete/data.json"))
        body (js/await (.json response))]
    body))

(js/console.log (js/await (fetch-data)))

;; (defn- format-l4-item [item]
;;   {:label (:label item) :items []}) ; TODO: Match L6 item.

;; (defn- l4-item-matches [item]
;;   true)

;; (defn- match-l4-item [item]
;;   (when (l4-item-matches item) (format-l4-item item)))

;; (defn- filter-items [search-term]
;;   (filter identity
;;           (map (fn [l4-item] (match-l4-item l4-item)) data)))

(defn handle-message [event]
  ;; (let [search-term (.-data event)]
  ;;   (js/console.log :search-term search-term)
  ;;   (js/postData (filter-items search-term))))
  (js/console.log "Worker received:" #js {:term event.data})
  (js/postMessage #js {:a 1}))

(set! js/self.onmessage handle-message)
