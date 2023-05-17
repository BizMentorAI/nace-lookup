; TODO: Does Cherry has other way of doing this?
(js* "function set_property(object, property, value) {
        object[property] = value
      }")

(defn showBlock [& args]
  (doseq [element (remove #(not (. % -nodeName)) args)]
    (set-property element.style "display" "block")))

(defn- set-content [element content]
  (cond
    (string? content)
    (set! (.-innerText element) content)

    (. content -nodeName)
    (.appendChild element content)

    (vector? content)
    (doseq [i content] (set-content element i))))

(defn- set-style [element style]
  (doseq [[property value] style]
    (set-property element.style (name property) value)))

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
       (case key
         :html  (set! (. element -innerHTML) value)
         :class (set! (. element -className) value)
         :style (set-style element value)
         (.setAttribute element (name key) value)))

     (when content (set-content element (js->clj content)))

     element)))

(defn createStyleLink [path]
  (tag "link" #js {:rel "stylesheet" :href path}))

; Can I use import map with workers?
(defn createWorker [path msg-handler err-handler]
  ; const name = path.replace(/.*\/([^/]+)\.js$/, '$1')
  (let [worker (new js/Worker path #js {:type "module"})]
    ;(.addEventListener worker "message" #(js/console.log "Message" %))
    (when msg-handler
      (.addEventListener worker "message" msg-handler))

    (.addEventListener worker "messageerror" #(js/console.log "Message can't be decoded" %))

    (.addEventListener worker "error" #(js/console.error "Worker error" %))
    (when err-handler
      (.addEventListener worker "error" err-handler))
    worker))

(defn tap [value fun]
  (fun value) value)
