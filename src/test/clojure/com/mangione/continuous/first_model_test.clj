(ns com.mangione.continuous.first-model-test
  (:require [clojure.test :refer :all])
  (use clojure.core$junit)

  (deftest addition
           (is (= 4 (+2 2)))))
