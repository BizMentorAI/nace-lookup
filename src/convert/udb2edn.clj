#!/usr/bin/env bb

(require '[clojure.pprint :refer [pprint]])

(defn trim-line [line]
  (-> (str/trim line)
      (str/replace #"^\d{12}\s+EN\s+" "")
      (str/replace #"(\d{4}) (\d{2}) (\d{2})" "$1$2$3")))

(defn process-line [line]
  (let [text (trim-line line)
        match (re-find #"^(\d{8})\s+(.+)\s*" text)]
    {:code (get match 1) :desc (get match 2)}))

(let [data (slurp "src/data/cn2023.udb")
      lines (str/split data #"\r\n")
      items (remove #(or (nil? (:code %)) (nil? (:desc %))) (map process-line lines))]
  (spit "src/data/cn2023.edn" (with-out-str (pprint (vec items))))
  ,)
