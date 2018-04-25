(ns com.mangione.continuous.variables.bound_variable)

(defn create [name, column, transformation]
  [name, column, transformation])

(defn name [bound] (get bound 0))
(defn column [bound] (get bound 1))
(defn calc [bound, x] (apply (get bound 2) [x]))