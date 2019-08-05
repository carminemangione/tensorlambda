(ns com.mangione.continuous.tools.utils-test
  (:require [clojure.test :refer :all])
  (:require [com.mangione.continuous.tools.utils :refer [single-op identity-func bi-op]]))

(deftest single-op-test
  (is (= [4 (.apply (single-op (fn [x] x)) 4)])))

(deftest identity-test
  (is (= [4 (identity-func 4)])))

(deftest bi-op-test
  (is (= [7 (.apply (bi-op (fn [x y] (+ x y))) 4 3)])))

(run-tests)
