(ns workers.autocomplete.transducer-test
  (:require [clojure.test :refer [deftest testing is run-tests]]
            [workers.autocomplete.transducer :refer [transducer]]))

; rf stands for reducing function.
; https://clojure.org/reference/transducers
(let [rf (transducer "book" conj)]
  (prn (rf))
  (prn (rf []))
  (prn (rf [] {})))

(deftest transducer-test
  (testing "..."
    (testing ""
      (is (= "" "")))))

; bb -cp src src/workers/autocomplete/transducer_test.cljc
; clj -M src/workers/autocomplete/transducer_test.clj
(run-tests 'workers.autocomplete.transducer-test)
