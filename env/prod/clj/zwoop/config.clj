(ns zwoop.config
  (:require [taoensso.timbre :as timbre]))

(def defaults
  {:init
   (fn []
     (timbre/info "\n-=[zwoop started successfully]=-"))
   :middleware identity})
