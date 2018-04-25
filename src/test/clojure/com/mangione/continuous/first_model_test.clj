(ns com.mangione.continuous.first_model_test
  (:require [com.mangione.continuous.first_model :as first_model]
              [clojure.test :refer :all]))

(deftest addition
  (is (= 4 (+ 2 2)))
  (is (= 7 (+ 3 4))))

(deftest codger_test
  (is (=  ["Get off my lawn, Sam" (first_model/codger "Sam")] )))

(run-tests)