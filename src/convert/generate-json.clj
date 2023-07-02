#!/usr/bin/env bb

(require '[clojure.edn :as edn])
(require '[clojure.walk :refer [postwalk]])

(def source-path "public/workers/autocomplete/data.edn")
(def target-path "public/workers/autocomplete/data.json")
(def records (edn/read-string (slurp source-path)))

; TODO: Delete empty syn/rel/exc.
; code -> id
(def processed-records
  (postwalk (fn [i]
              (if (and (map? i) (:extra i))
                (dissoc i :extra) i))
            records))

(spit target-path (json/generate-string
                   processed-records
                   {:pretty true}))
