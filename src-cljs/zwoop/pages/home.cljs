(ns zwoop.pages.home
  (:require
   [zwoop.core :refer (chsk-send!)]
   [reagent.core :as r]
   [reagent.session :as session]
   [taoensso.timbre :as timbre]
   [secretary.core :as secretary]))

(def key-map
  {84 #(secretary/dispatch! "#/trader")
   79 #(chsk-send! [::test])
   })

(defn home-page []
  (session/put! :key-map key-map)
  [:div.container
   [:div.row
    [:div.col-md-6.panel.panel-default
     [:div.panel-body "This is the main page"]]
    [:div.col-md-6.panel.panel-default
     [:div.panel-body ""]]
    ]
   [:div.row
    [:div.col-md-4.panel.panel-default
     [:div.panel-body "Hit t to navigate to new page."]]
    [:div.col-md-4.panel.panel-default
     [:div.panel-body "Hit o to send message to the server."]]
    [:div.col-md-4.panel.panel-default
     [:div.panel-body "Expected result is to see a log message on the server side."]]]])


