(ns com.mangione.continuous.first_model
  (:gen-class))

(defn error-message [severity]
  str "OH GOD! IT'S A DISASTER! WE'RE "
  (if (= severity :mild)
    "MILDLY INCONVENIENCED"
    "GOING TO DIE!!!!!"))

(defn codger-communication
  [whippersnapper]
  (str "Get off my lawn, " whippersnapper))

(defn codger
  [& whippersnappers]
  (map codger-communication whippersnappers))


(defn fib
   [number]
   (loop [res [0 1]]
     (if (>= (count res) number)
       res
       (recur (conj res (+' (last res) (last (butlast res))))))))

(defn titleize
  [topic]
  (str topic " for the brave and the true"))