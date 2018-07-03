(ns com.mangione.continuous.variables.variables-test
  (:require [clojure.test :refer :all])
  (:require [com.mangione.continuous.variables.bound_variable :as bound_variable :refer [create]])
  (:import (com.mangione.continuous.variables BoundVariable)))

(defn myfunc [var] (inc var))

(deftest bound_variable-create
  (print (create "var_name" 2 myfunc))
  (is ["var_name" 2 myfunc] (create "var_name" 2 (resolve 'myfunc))))

(deftest name-test
  (is "var_name" (bound_variable/name (create "var_name" 2 myfunc))))

(deftest apply-test
  (is 4 (bound_variable/calc ["var_name" 2 myfunc] 3)))


(.execute (BoundVariable.) (bound_variable/my-function))



(run-tests)
