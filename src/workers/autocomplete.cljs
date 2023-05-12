(let [response (js/await (js/fetch "/workers/autocomplete/data.json"))
      body (js/await (.-body response))
      reader (.getReader body)]
  ;; (js/console.log :response body reader {:read (.read reader)})
  )

;; (defn- format-l4-item [item]
;;   {:label (:label item) :items []}) ; TODO: Match 6 item.

;; (defn- l4-item-matches [item]
;;   true)

;; (defn- match-l4-item [item]
;;   (when (l4-item-matches item) (format-l4-item item)))

;; (defn- filter-items [search-term]
;;   (filter identity
;;           (map (fn [l4-item] (match-l4-item l4-item)) data)))

;; ; ^js
(defn- onmessage [event])
;; (let [search-term (.-data event)]
  ;;   (js/console.log :search-term search-term)
  ;;   (js/postData (filter-items search-term))))

(set! js/self.onmessage
      (fn [event]
        (js/console.log "Worker received:" event.data)
        (js/postMessage #js {:a 1})
        ,))
