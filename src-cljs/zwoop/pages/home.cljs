(ns zwoop.pages.home
  (:require
   ;; [goog.events :as events]
   ;; [zwoop.core :refer (chsk-send!)]
   [reagent.session :as session]
   [taoensso.timbre :as timbre]
   [secretary.core :as secretary]))

(def key-map
  {84 #(secretary/dispatch! "#/trader")
   ;; 79 #(chsk-send! [::test])
   })

(defn home-page []
  (session/put! :key-map key-map)
  [:div.container
   [:div.row
    [:div.col-md-12
     "This is the main page, hit t to navigate to trader page."]]])
