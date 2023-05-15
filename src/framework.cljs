(defn set-content [element content]
  (cond
    (string? content)
    (set! element -innerHTML content)

    (. content -nodeName)
    (element.appendChild content)

    (. content -forEech)
    (doseq [i content] (set-content element i))))

(defn tag
  "..."
  ([name] (tag name {} nil))

  ([name arg]
   (if (map? arg) (tag name arg nil) (tag name {} arg)))

  ([name opts content]
   (let [element (if opts.is
                   (js/document.createElement name {:is opts.is})
                   (js/document.createElement name))]
     (doseq [[key value] opts] (set! element key value))
     (when content (set-content element content)))))

(defn createStyleLink [path]
  (tag "link" {:rel "stylesheet" :href path}))

; Can I use import map with workers?
(defn createWorker [path msg-handler err-handler]
  ; const name = path.replace(/.*\/([^/]+)\.js$/, '$1')
  (let [worker (new js/Worker path, {:type "module"})]
    (worker.addEventListener "message" msg-handler)
    (worker.addEventListener "error" #(js/console.error "Worker error" %))
    (worker.addEventListener "error" err-handler)
    worker)
)
(defn tap [value fn]
  (fn value) value)
