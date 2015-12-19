(ns zwoop.pages.trader
  (:require
   ;; [goog.events :as events]
   [reagent.session :as session]
   [secretary.core :as secretary]))

(def key-map
  {72 #(secretary/dispatch! "#/")})

(defn trader-page []
  (session/put! :key-map key-map)
  [:div.container
   [:div.row
    [:div.col-md-12
     "New page - hit h to navigate back!"]]])
