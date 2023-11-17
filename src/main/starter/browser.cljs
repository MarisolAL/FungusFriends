(ns starter.browser
  (:require
   [reagent.dom :as rd]))

(defn- hello-world []
  [:ul
   [:li "Hello"]
   [:li {:style {:color "red"}} "World!"]])

;; start is called by init and after code reloading finishes
(defn ^:dev/after-load start []
  (rd/render [hello-world] (js/document.getElementById "app")))



(defn init []
  ;; init is called ONCE when the page loads
  ;; this is called in the index.html and must be exported
  ;; so it is available even in :advanced release builds
  (js/console.log "init")
  (start))

;; this is called before any code is reloaded
(defn ^:dev/before-load stop []
  (js/console.log "stop"))
