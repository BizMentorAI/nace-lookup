(ns macros)

(defmacro inline-resource [path]
  (slurp path))
