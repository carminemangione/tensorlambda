(ns com.mangione.continuous.tools.utils
  (:import (java.util.function Function BiFunction)))

(defn ^Function single-op [f]
  (reify Function
    (apply [this arg]
      (f arg))))

(defn ^BiFunction bi-op [f]
  (reify BiFunction
    (apply [this arg1 arg2]
      (f arg1 arg2))))

(defn identity-func [i] i)