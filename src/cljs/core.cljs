(ns core
  (:require
   ["leaflet" :as leaflet]
   [re-frame.core :as rf]
   [reagent.dom :as rd]
   [reagent.core :as rg]
   ["./front-end-api" :as fungus-api]
   [fungus-friends :as ff]
   [components.leaflet-map.views :as l-map]))

(def geometries (atom [{:type :polygon ;; Las figuras a pintar, poner hongos aqui
                        :coordinates [[65.1 25.2]
                                      [65.15 25.2]
                                      [65.125 25.3]]}

                       {:type :line
                        :coordinates [[65.3 25.0]
                                      [65.4 25.5]]}]))

(def view-position (atom [65.1 25.2]))
(def zoom-level (atom 8))

(defn- hello-world []
  (let [_ (println )]
    [:div
     [ff/page]
     ;;[l-map/create-map]
     ])
  #_[:ul
     [:li "Hello"]
     [:li {:style {:color "red"}} "World!"]])

(defn demo []
  (let [_ (println @view-position @zoom-level)]
    (fn []
      [:div
       [l-map/leaflet {:id     "kartta"
                       :width  "60%"
                       :height "50%"
                       :view   view-position
                       :zoom   zoom-level

                       ;; The actual map data (tile layers from OpenStreetMap), also supported is
                       ;; :wms type
                       :layers [{:type        :tile
                                 :url         "http://{s}.tile.osm.org/{z}/{x}/{y}.png"
                                 :attribution "&copy; <a href=\"http://osm.org/copyright\">OpenStreetMap</a> contributors"}]

                       ;; Geometry shapes to draw to the map
                       :geometries geometries

                       ;; Add handler for map clicks
                       :on-click #(println "map clicked")}]
       #_[:div.actions
        "Control the map zoom by swap!ing the atoms"
        [:br]
        [:button {:on-click #(swap! zoom-level inc)} "zoom in"]
        [:button {:on-click #(swap! zoom-level dec)} "zoom out"]]])))



;; start is called by init and after code reloading finishes
(defn ^:dev/after-load start []
  (let [_ (println "f" fungus-api)]
    (rd/render [demo] (js/document.getElementById "app"))
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
