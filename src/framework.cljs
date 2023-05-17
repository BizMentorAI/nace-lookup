; TODO: Does Cherry has other way of doing this?
(js* "function set_property(object, property, value) {
        object[property] = value
      }")

(defn element? [thing]
  (boolean (. thing -nodeName)))

(defn hide [element]
  (set-property element.style "display" "none")
  element)

; Turns out that it's impossible to delete a property from the style object.
;(js-delete element.style "display")
(defn unhide [element]
  (set-property element.style "display" "")
  element)

(defn unhideAll [& args]
  (clj->js (map unhide (filter element? args))))

(defn- set-content [element content]
  (cond
    (string? content)
    (set! (.-innerText element) content)

    (element? content)
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

;; // Fix page height for mobile Safari.
;; // Doing this in CSS doesn't work as Chrome matches it also.
;; // https://allthingssmitty.com/2020/05/11/css-fix-for-100vh-in-mobile-webkit
;; if (this.isMobileSafari()) {
;;   this.style.minHeight = "-webkit-fill-available"
;; }
;;
;; isMobileSafari() {
;;   const ua = window.navigator.userAgent
;;   return ua.match(/iPhone|iPad/i) && ua.match(/WebKit/i) && !ua.match(/CriOS/i)
;; }
