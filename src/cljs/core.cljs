(ns core
  (:require
   [events :as events]
   [subs :as subs]
   [re-frame.core :as rf]
   [reagent.dom :as rd]
   [reagent.core :as rg]
   ["./front-end-api" :as fungus-api]
   [fungus-friends.views :as ff]))


(defn- hello-world []
  (rg/with-let [a (rf/subscribe [::subs/db])
                b (rg/atom nil)]
    (let [fn-p (fn []
                 (fungus-api/default)
                 (.then (fungus-api/default)
                        (fn [js-info]
                          (reset! b js-info))))
          _    (fn-p)
          _    (rf/dispatch [::events/set-mushrooms-data @b])
     ;;     _    (println "DB --> " @a)
          ]
      [:div
       [ff/page]])))


;; start is called by init and after code reloading finishes
(defn ^:dev/after-load start []
  (rd/render [hello-world] (js/document.getElementById "app")))



(defn init []
  ;; init is called ONCE when the page loads
  ;; this is called in the index.html and must be exported
  ;; so it is available even in :advanced release builds
  (js/console.log "init")
  (rf/dispatch-sync [::events/initialize-app])
  (start))

;; this is called before any code is reloaded
(defn ^:dev/before-load stop []
  (js/console.log "stop"))
