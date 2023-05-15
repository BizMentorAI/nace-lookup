(defn- set-content [element content]
  (cond
    (string? content)
    (set! (.-innerHTML element) content)

    (. content -nodeName)
    (.appendChild element content)

    (vector? content)
    (doseq [i content] (set-content element i))))

(defn- create-element [name opts]
  (if (:is opts)
    (js/document.createElement name #js {:is (:is opts)})
    (js/document.createElement name)))

(defn tag
  "Create new DOM element."
  ([tname] (tag tname {} nil))

  ([tname arg]
   (if (map? (js->clj arg)) (tag tname arg nil) (tag tname {} arg)))

  ([tname opts content]
   (let [opts (js->clj opts {:keywordize-keys true})
         element (create-element tname opts)]

     (doseq [[key value] (dissoc opts :is)]
       (.setAttribute element (name key) value))

     (when content (set-content element (js->clj content)))

     element)))

(defn createStyleLink [path]
  (tag "link" #js {:rel "stylesheet" :href path}))

; Can I use import map with workers?
(defn createWorker [path msg-handler err-handler]
  ; const name = path.replace(/.*\/([^/]+)\.js$/, '$1')
  (let [worker (new js/Worker path #js {:type "module"})]
    (worker.addEventListener "message" msg-handler)
    (worker.addEventListener "error" #(js/console.error "Worker error" %))
    (worker.addEventListener "error" err-handler)
    worker))

(defn tap [value fun]
  (fun value) value)
