(ns com.mangione.continuous.first_model (:gen-class))

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