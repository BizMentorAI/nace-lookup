#!/usr/bin/env bb -cp src/convert

(require '[inflections :refer (plural)])

(def sg-nouns (into #{} (str/split-lines (slurp "src/data/nounlist.txt"))))

(spit "src/data/dict.edn" (pr-str (into #{} (flatten (map #(vec [%, (plural %)]) sg-nouns)))))
