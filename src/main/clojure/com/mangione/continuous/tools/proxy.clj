(ns com.mangione.continuous.tools.proxy
  (:require [clojure.java.io :as io])
  (:import (com.mangione.continuous.observations StringObservationFactory ProxyValues Observation)
           (com.mangione.continuous.calculators VariableCalculator VariableCalculations)
           (com.mangione.continuous.observationproviders CsvObservationProvider VariableCalculatorObservationProvider RowFilteringObservationProvider)
           (java.io File)))



(defn csv_provider [filename] (CsvObservationProvider.
                                (File. (.toURI (io/resource filename)))
                                (StringObservationFactory.) true))

(def mapOfTracks (hash-map))

(def provider (RowFilteringObservationProvider. (csv_provider "sdlc_test_execution.csv")))

(def iterator (.iterator provider))

(defn fill-map []
  (while (.hasNext iterator)
    (def lineData (.getFeatures (.next iterator)))
    (if (> (.size lineData) 6)
      (if (not (contains? mapOfTracks (nth lineData 6)))
        (def mapOfTracks (assoc mapOfTracks (nth lineData 6) (.size mapOfTracks)))
        )
      )

    )
  (println "MAP FILLED")
  )

(defn proxy-calc []
  (reify VariableCalculator
    (apply [this arg]
      (vec (concat (conj (vec (replicate (get mapOfTracks arg) 0)) 1) (vec (replicate (- (.size mapOfTracks) (get mapOfTracks arg)) 0))))
      )
    )
  )

(def var_calc (VariableCalculations. {6 (proxy-calc)} nil))

(def feature_map [])

(def vcop (VariableCalculatorObservationProvider. provider var_calc (StringObservationFactory.)))

(defn create_named_columns []
  (.fillNamedColumns vcop mapOfTracks)
  )

(defn create_feature_map []
  (fill-map)
  (def iterator2 (.iterator vcop))
  (dotimes [i 10]
    (def feature_map (conj feature_map (.getFeatures (.next iterator2))))
    )
  (create_named_columns)
  (.setFeatureMap vcop feature_map)
  )


(create_feature_map)