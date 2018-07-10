(ns com.mangione.continuous.integration-test-model
  (:require [clojure.test :refer :all]
            [clojure.java.io :as io])

  (import (com.mangione.continuous.observationproviders CsvObservationProvider)
          (com.mangione.continuous.observations StringObservationFactory)
          (java.io File)))

(defn csv_provider [filename] (CsvObservationProvider.
                                (File. (.toURI (io/resource filename)))
                                (StringObservationFactory.) true))
(def provider (csv_provider "sdlc_test_execution.csv"))

(deftest test-open-csv (is 50006 (.getNumberOfLines provider)))

(def mapOfTracks (hash-map))

(deftest test-valid-map (is 0 (count mapOfTracks)))

(def iterator (.iterator provider))

(defn printing-sorted [results]

  (into (sorted-map-by (fn [key1 key2]
                         (compare [(get results key2) key2]
                                  [(get results key1) key1])))
        results)
  )

(defn print-stuff [mapOfStuff]
  (for [elem mapOfStuff]
    (println elem)
    )

  )

(deftest fill-map

  (while
    (.hasNext iterator)
    (def lineData (.getFeatures (.next iterator)))

    (if (> (.size lineData) 6)
      (def mapOfTracks (assoc mapOfTracks (nth lineData 6) (+ 1 (get mapOfTracks (nth lineData 6) 0))))


      )

    )
  (print (print-stuff (printing-sorted mapOfTracks)))
  )





(run-tests)


