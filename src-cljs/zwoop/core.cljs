(ns zwoop.core
  (:require-macros
   [cljs.core.async.macros :as asyncm :refer (go go-loop)])
  (:require
   [zwoop.pages.home :refer (home-page)]
   [zwoop.pages.trader :refer (trader-page)]
   [reagent.core :as reagent]
   [reagent.session :as session]
   [secretary.core :as secretary :include-macros true]
   [taoensso.timbre :as timbre]
   [goog.events :as events]
   [goog.history.EventType :as HistoryEventType]
   [cljs.core.async :as async :refer (<! >! put! chan)]
   [taoensso.sente  :as sente :refer (cb-success?)] ; <--- Add this
  )
  (:import goog.History
           [goog.events KeyHandler]
           [goog.events.KeyHandler EventType]))

;;; Add this: --->
(let [{:keys [chsk ch-recv send-fn state]}
      (sente/make-channel-socket! "/chsk" ; Note the same path as before
       {:type :auto ; e/o #{:auto :ajax :ws}
       })]
  (def chsk       chsk)
  (def ch-chsk    ch-recv) ; ChannelSocket's receive channel
  (def chsk-send! send-fn) ; ChannelSocket's send API fn
  (def chsk-state state)   ; Watchable, read-only atom
  )

(def pages
  {:home #'home-page
   :trader #'trader-page})

(defn page []
  [(pages (session/get :page))])
;; -------------------------
;; Routes
(secretary/set-config! :prefix "#")

(secretary/defroute "/" []
  (session/put! :page :home))

(secretary/defroute "/trader" []
  (session/put! :page :trader))

;; -------------------------
;; History
;; must be called after routes have been defined
(defn hook-browser-navigation! []
  (doto (History.)
        (events/listen
          HistoryEventType/NAVIGATE
          (fn [event]
              (secretary/dispatch! (.-token event))))
        (.setEnabled true)))

;; -------------------------
;; Initialize app
(defn mount-components []
  (reagent/render [#'page] (.getElementById js/document "app")))

(defn execute-key [event]
  "Extracts the function associated with component and key and executes the function"
  (let [key (.-keyCode event)]
    (timbre/info key)
    (when-let [f (get (session/get :key-map) key)]
      (f))))

(defn keyboard-events []
  (events/listen (KeyHandler. js/document) EventType.KEY execute-key))

(defn init! []
  (keyboard-events)
  (hook-browser-navigation!)
  (mount-components))
