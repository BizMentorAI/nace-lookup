#!/usr/bin/env bb

(require '[babashka.pods :as pods])
(pods/load-pod 'org.babashka/fswatcher "0.0.3")

(require '[pod.babashka.fswatcher :as fw])
(require '[babashka.process :refer [shell]])

;(fw/watch "src" prn {:delay-ms 5000})
;(def watcher (fw/watch "src" (fn [event] (prn event))))
;{:type :write|chmod, :path "src/workers/autocomplete.cljs"}

(def write-events #{:create :write :write|chmod})

(defn compile [{:keys [type path] :as event}]
  (when (and (write-events type) (re-find #"/[a-z-]+\.cljs$" path))
    (shell (str "npx cherry compile " path))))

(defn move [{:keys [type path] :as event}]
  (when (and (write-events type) (re-find #"\.mjs$" path))
    (let [js-path (str/replace path #"\.mjs$" ".js")
          public-path (str/replace js-path "src" "public")]
      (println "~ Moving" path "->" public-path)
      ; This will fail if the dir hasn't been created yet, whatever.
      (shell "mv" path public-path))))

(fw/watch "src" prn {:recursive true})
(fw/watch "src" compile {:recursive true})
(fw/watch "src" move {:recursive true})

(println "~ Watching src.\n")
; compile src/workers/autocomplete.cljs && cat src/workers/autocomplete.mjs | sed 's/cherry-cljs\/lib/\/js/' > public/workers/autocomplete/autocomplete.js && bat public/workers/autocomplete/autocomplete.js
@(promise)
