(ns core
  (:require
   [re-frame.core :as rf]
   [reagent.dom :as rd]
   [reagent.core :as rg]
   ["./front-end-api" :as fungus-api]
   [fungus-friends :as ff]
   [components.leaflet-map.views :as l-map]))

(def geometries (atom [{:type        :polygon ;; Las figuras a pintar, poner hongos aqui
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

(def view-position (atom [65.1 25.2]))
(def zoom-level (atom 8))

(defn demo []
  (fn []
    [:div {:style {:width  "400px"
                   :height "400px"}}
     [l-map/leaflet {:id         "kartta"
                     :width      "60%"
                     :height     "50%"
                     :view       view-position
                     :zoom       zoom-level
                     :layers     [{:type        :tile
                                   :url         "http://{s}.tile.osm.org/{z}/{x}/{y}.png"
                                   :attribution "&copy; <a href=\"http://osm.org/copyright\">OpenStreetMap</a> contributors"}]
                     :geometries geometries
                     ;; :on-click   #(println "map clicked")
                     }]]))


(defn- hello-world []
  [:div
   [ff/page]
   [demo]])


;; start is called by init and after code reloading finishes
(defn ^:dev/after-load start []
  (let [_ (println "f" fungus-api)]
    (rd/render [hello-world] (js/document.getElementById "app"))
    ))



(defn init []
  ;; init is called ONCE when the page loads
  ;; this is called in the index.html and must be exported
  ;; so it is available even in :advanced release builds
  (js/console.log "init")
  (start))

;; this is called before any code is reloaded
(defn ^:dev/before-load stop []
  (js/console.log "stop"))
