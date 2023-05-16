(ns macros
  (:require ["fs" :as fs]))

(defmacro inline-resource [path]
  (fs/readFileSync path))
