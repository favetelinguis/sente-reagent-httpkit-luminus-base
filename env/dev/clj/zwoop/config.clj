(ns zwoop.config
  (:require [selmer.parser :as parser]
            [taoensso.timbre :as timbre]
            [zwoop.dev-middleware :refer [wrap-dev]]))

(def defaults
  {:init
   (fn []
     (parser/cache-off!)
     (timbre/info "\n-=[zwoop started successfully using the development profile]=-"))
   :middleware wrap-dev})
