(ns macros
  (:require ["fs" :as fs]))

;; (prn fs)
;; (defmacro inline-resource [path]
;;   (js/console.log "Path" path) (prn "Path" path)
;;   (fs/readFileSync path)
;;   ,)

(defmacro inline-readme []
  (fs/readFileSync "/Users/jakub/Documents/Projects/bizmentor/nace-finder-modern/README.org"))
