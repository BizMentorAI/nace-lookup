(ns workers.autocomplete.autocomplete
  (:require-macros [macros :refer [inline-resource]]))

; Does inlining work?
(def r (inline-resource "/Users/jakub/Documents/Projects/bizmentor/nace-finder-modern/README.org"))
(js/console.log r)



(defn ^:async fetch-data []
                                        ; TODO: The JSON path should come from a variable (tangling).
  (let [response (js/await (js/fetch "/workers/autocomplete/data.json"))
        body (js/await (.json response))] body))

(def data (js->clj (js/await (fetch-data)) {:keywordize-keys true}))

(defn- filter-items [data search-term]
  ;(persistent! (into [] (partial data-transducer search-term) data))
  []
  )

(defn handle-message [event]
  (let [search-term event.data]
    (js/console.log "Worker received:" #js {:term search-term})
    (let [result (filter-items data search-term)]
      (js/console.log "Worker returned:" (clj->js result))
      (js/postMessage (clj->js result)))))

(set! onmessage handle-message)
