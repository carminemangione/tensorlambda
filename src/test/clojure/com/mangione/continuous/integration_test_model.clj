(ns com.mangione.continuous.integration-test-model
  (:require [clojure.test :refer :all]
            '[clojure.java.io :as io])

  (import '(com.mangione.continuous.observationproviders CsvObservationProvider)
          '(com.mangione.continuous.observations StringObservationFactory)
          (java.io File))

  (defn csv_provider[filename] (CsvObservationProvider.
                                 (File. (.toURI (io/resource filename)))
                                 (StringObservationFactory.)))


  )
