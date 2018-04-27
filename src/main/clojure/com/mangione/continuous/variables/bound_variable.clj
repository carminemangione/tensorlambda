(ns com.mangione.continuous.variables.bound_variable
  (:import (java.util.function Function)))

(defn create [name, column, transformation]
  [name, column, (apply transformation [1])])

(defn name [bound] (get bound 0))
(defn column [bound] (get bound 1))
(defn calc [bound, x] (apply (get bound 2) [x]))

(defn my-function []
  (reify
    Function
    (apply [this arg]
      (inc arg))))


(defn ^Function as-function [f]
  '(reify Function
    (apply [arg] (f arg))))

(defmacro jfn [& args]
  `(as-function (fn ~@args)))



