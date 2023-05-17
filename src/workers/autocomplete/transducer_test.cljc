(ns workers.autocomplete.transducer-test
  (:require [clojure.test :refer [deftest testing is run-tests]]))

(deftest month-formatting
  (testing "next-month"
    (testing "returns next month (Nov -> Dec)"
      (is (= "" "")))))

; bb src/workers/autocomplete/transducer_test.cljc
; clj -M src/workers/autocomplete/transducer_test.clj
(run-tests 'workers.autocomplete.transducer-test)
