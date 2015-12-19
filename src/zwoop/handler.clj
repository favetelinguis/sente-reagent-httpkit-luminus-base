(ns zwoop.handler
  (:require [compojure.core :refer [defroutes routes wrap-routes]]
            [zwoop.layout :refer [error-page]]
            [zwoop.routes.home :refer [home-routes]]
            [zwoop.routes.websockets :as ws :refer [websocket-routes start-router! stop-router!]]
            [zwoop.middleware :as middleware]
            [compojure.route :as route]
            [taoensso.timbre :as timbre]
            [taoensso.timbre.appenders.3rd-party.rotor :as rotor]
            [selmer.parser :as parser]
            [environ.core :refer [env]]
            [zwoop.config :refer [defaults]]
            [mount.core :as mount]))

(defn init
  "init will be called once when
   app is deployed as a servlet on
   an app server such as Tomcat
   put any initialization code here"
  []

  (timbre/merge-config!
    {:level     ((fnil keyword :info) (env :log-level))
     :appenders {:rotor (rotor/rotor-appender
                          {:path (or (env :log-path) "zwoop.log")
                           :max-size (* 512 1024)
                           :backlog 10})}})
  (start-router!)
  (timbre/info "Router started" @ws/router_)
  (doseq [component (:started (mount/start))]
    (timbre/info component "started"))
  ((:init defaults)))

(defn destroy
  "destroy will be called when your application
   shuts down, put any clean up code here"
  []
  (timbre/info "zwoop is shutting down...")
  (stop-router!)
  (doseq [component (:stopped (mount/stop))]
    (timbre/info component "stopped"))
  (timbre/info "shutdown complete!"))

(def app-routes
  (routes
    websocket-routes
    (wrap-routes #'home-routes middleware/wrap-csrf)
    (route/not-found
      (:body
        (error-page {:status 404
                     :title "page not found"})))))

(def app (middleware/wrap-base #'app-routes))
