(ns com.mangione.continuous.variables.variables-test
  (:require [clojure.test :refer :all])
  (:require [com.mangione.continuous.variables.bound_variable :as bound_variable :refer [create]]))

(defn myfunc [var] (inc var))
(deftest bound_variable-create
  (print (create "var_name" 2 myfunc))
  (is ["var_name" 2 myfunc] (create "var_name" 2 myfunc)))

(deftest name-test
  (is "var_name" (bound_variable/name (create "var_name" 2 myfunc))))

(deftest apply-test
  (is 4 (bound_variable/calc (create "var_name" 2 myfunc) 3)))

(run-tests)
