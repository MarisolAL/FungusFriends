(ns core
  (:require
   [events :as events]
   [subs :as subs]
   [re-frame.core :as rf]
   [reagent.dom :as rd]
   [reagent.core :as rg]
   ["./front-end-api" :as fungus-api :refer (mushrooms)]
   [fungus-friends :as ff]))

#_(def geometries (atom [{:type        :polygon ;; Las figuras a pintar, poner hongos aqui
                          :coordinates [[65.1 25.2]
                                        [65.15 25.2]
                                        [65.125 25.3]]
                          :popup-msg   "Polygon"
                          :color       "#E3C2E5"}

                         {:type        :line
                          :coordinates [[65.3 25.0]
                                        [65.4 25.5]]
                          :popup-msg   "Line"
                          :color       "#431E70"}
                         {:type        :icon
                          :icon-url    "/img/dimitri.jpg"
                          :coordinates [[65.1 25.2]]
                          :popup-msg   "Icon"}]))

;;(def view-position (atom [65.1 25.2]))
;;(def zoom-level (atom 8))




(defn- hello-world []
  (rg/with-let [a (rf/subscribe [::subs/db])]
    (let [
          _ (println "f --> " fungus-api)
          _ (println "Promise " (fungus-api/default))
          fn-p (fn []
                 (fungus-api/default)
                 (.then (fungus-api/default)
                        (fn [resultado]
                          (println " datos " resultado))) ;; FUNCIONA
                 )
          _ (fn-p)
          _ (println "DB --> " @a)]
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
