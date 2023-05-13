(defn ^:async fetch-data []
  (let [response (js/await (js/fetch "/workers/autocomplete/data.json"))
        body (js/await (.json response))]
    body))

(def data (js/await (fetch-data)))

; :js/keys [a] for JS keys destructuring.
;; (defn- format-l4-item [item]
;;   {:label (:label item) :items []}) ; TODO: Match L6 item.

;; (defn- l4-item-matches [item]
;;   true)

;; (defn- match-l4-item [item]
;;   (when (l4-item-matches item) (format-l4-item item)))

(defn- filter-items [search-term]
  {:a 7})
  ;; (filter identity
  ;;         (map (fn [l4-item] (match-l4-item l4-item)) data)))

(defn handle-message [event]
  (let [search-term event.data]
    (js/console.log "Worker received:" #js {:term search-term})
    (let [result (filter-items search-term)]
      (js/postMessage (clj->js result)))))

(set! js/self.onmessage handle-message)
