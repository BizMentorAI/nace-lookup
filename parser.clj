; @hiredman:
; this is sort of definitionally what a parser does, takes a sequence of things and turns it into a tree
; abstractly the grammar might be something like:

; X = A(1)*
; A(n) = thing-with-level(n) A(n+1)*

; like spec for example can be used as a parser over sequeces of data structures
; https://downey.family/p/2023-05-15/parsing.clj.html is an example with some slapped together parser combinators, almost certainly has some bugs, but the input and outputs match your examples

; you can also check out https://www.juxt.pro/blog/parsing-with-clojure-spec/ for doing it with spec instead of rolling your own parser
; it is using spec to parse text, but shows spec for parsing and can all be generalized to parsing a sequence of data structures
; I am not sure, but you might need to get really creative with spec (maybe abusing multi-spec?) for the level shifting

(defn parse [f]
  (fn [input-sequence]
    (when (and (seq input-sequence)
               (f (first input-sequence)))
      input-sequence)))

(defn parse-thing-with-level [level]
  (parse (fn [input] (= level (:level input)))))

(defn ordered-choice [a b]
  (fn [input-sequence]
    (if-let [parsed (seq (a input-sequence))]
      parsed
      (b input-sequence))))

(defn sequence [a b]
  (fn [input-sequence]
    (when-let [parsed (seq (a input-sequence))]
      (when-let [parsed2 (seq (b (rest input-sequence)))]
        (cons [(first parsed) (first parsed2)] (rest parsed2))))))

(defn optional [a]
  (fn [input-sequence]
    (if (seq input-sequence)
      (if-let [parsed (seq (a input-sequence))]
        parsed
        (cons nil input-sequence))
      (cons nil input-sequence))))

(defn nothing [input-sequence]
  (cons nil input-sequence))

(defn reduction [p f]
  (fn [input-sequence]
    (if-let [parsed (seq (p input-sequence))]
      (cons (f (first parsed))
            (rest parsed))
      nil)))

(declare A)

(defn a-list [level]
  (fn [input-sequence]
    ((ordered-choice (reduction (sequence (A level)
                                          (a-list level))
                                (fn [[a b]]
                                  (cons a b)))
                     nothing)
     input-sequence)))

(defn A [level]
  (reduction (sequence (parse-thing-with-level level)
                       (a-list (inc level)))
             (fn [[a b]]
               (assoc a :items b))))

((requiring-resolve 'clojure.pprint/pprint)
 (first
  ((A 1)
   [{:level 1, :label "PRODUCTS OF AGRICULTURE, FORESTRY AND FISHING"}
    {:level 2, :code "01.11", :label "Cereals (except rice), leguminous crops and oil seeds"}
    {:level 3, :code "01.11.11", :label "Durum wheat"}
    {:level 3, :code "01.11.12", :label "Wheat, except durum wheat"}
    {:level 3, :code "01.11.20", :label "Maize"}])))

(defn process-data [data]
  ;(binding [*out* *err*] (prn (process-level 1 data)))
  (let [results ((A 1)
                 (process-level 1 data))]
    (filter #(= (:level %) 1) results)))
