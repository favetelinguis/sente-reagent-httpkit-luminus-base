(ns zwoop.routes.websockets
 (:require [compojure.core :refer [GET POST defroutes]]
           [org.httpkit.server
            :refer [send! with-channel on-close on-receive]]
           [taoensso.sente :as sente]
           [taoensso.sente.server-adapters.http-kit      :refer (sente-web-server-adapter)]
           [cognitect.transit :as t]
           [taoensso.timbre :as timbre]))

(let [{:keys [ch-recv send-fn ajax-post-fn ajax-get-or-ws-handshake-fn
              connected-uids]}
      (sente/make-channel-socket! sente-web-server-adapter {})]
  (def ring-ajax-post                ajax-post-fn)
  (def ring-ajax-get-or-ws-handshake ajax-get-or-ws-handshake-fn)
  (def ch-chsk                       ch-recv) ; ChannelSocket's receive channel
  (def chsk-send!                    send-fn) ; ChannelSocket's send API fn
  (def connected-uids                connected-uids) ; Watchable, read-only atom
  )

(defroutes websocket-routes
  (GET  "/chsk" req (ring-ajax-get-or-ws-handshake req))
  (POST "/chsk" req (ring-ajax-post                req))
  )
