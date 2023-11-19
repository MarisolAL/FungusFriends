(ns components.leaflet-map.views
  (:require
   ;;["leaflet" :as leaflet]
   [reagent.core :as rg]))


(defmulti create-shape :type)

(defmethod create-shape :polygon [{:keys [coordinates color]
                                   :or   {color "#D0376C"}}]
  (js/L.polygon (clj->js coordinates)
                #js {:color       color
                     :fillOpacity 0.5}))

(defmethod create-shape :line [{:keys [coordinates color]
                                :or   {color "#D0376C"}}]
  (js/L.polyline (clj->js coordinates)
                 #js {:color color}))

(defmethod create-shape :point [{:keys [coordinates color]
                                 :or   {color "#D0376C"}}]
  (js/L.circle (clj->js (first coordinates))
               10
               #js {:color color}))

#_(def icon-type
  (.extend
   (js/L.icon)
   #js
   {:options
    #js
    {:shadowUrl    "leaf-shadow.png",
     :iconSize     #js [38 95],
     :shadowSize   #js [50 64],
     :iconAnchor   #js [22 94],
     :shadowAnchor #js [4 62],
     :popupAnchor  #js [(- 3) (- 76)]}}))

#_(defmethod create-shape :icon [{:keys [coordinates]}]
  (let [icon (new icon-type #js {:iconUrl "leaf-green.png"})
        _ (println icon)]
    (js/L.icon (clj->js (first coordinates))
               10
               #js {:shadowUrl    "leaf-shadow.png",
                    :iconSize     #js [38 95],
                    :shadowSize   #js [50 64],
                    :iconAnchor   #js [22 94],
                    :shadowAnchor #js [4 62],
                    :popupAnchor  #js [(- 3) (- 76)]})))


(defn- remove-layers
  "Removes all shape objects that are required to."
  [{:keys [leaflet geometries-set geometries-map]}]
  (doseq [removed-shape (keep (fn [[geom shape]]
                                (when-not (geometries-set geom)
                                  shape))
                              geometries-map)]
    (.removeLayer leaflet removed-shape)))

(defn- add-new-shapes
  "Adds new required shapes to the map."
  [component-atm geometries]
  (let [{:keys [leaflet geometries-map]} (rg/state component-atm)]
    (loop [new-geometries-map  {}
           [geom & geometries] geometries]
      (if-not geom
        ;; Update component state with the new geometries map
        (rg/set-state component-atm {:geometries-map new-geometries-map})
        (if-let [existing-shape (geometries-map geom)]
          ;; Have existing shape, don't need to do anything
          (recur (assoc new-geometries-map geom existing-shape) geometries)

          ;; No existing shape, create a new shape and add it to the map
          (let [shape (create-shape geom)
                popup-msg (:popup-msg geom)]
            (.addTo shape leaflet)
            (when popup-msg
              (.bindPopup shape popup-msg))
            (recur (assoc new-geometries-map geom shape) geometries)))))))

(defn- update-leaflet-geometries
  "Updates the Leaflet layers based on the data, mutates the LeafletJS map object."
  [component-atm geometries]
  (let [{:keys [leaflet geometries-map]} (rg/state component-atm)
        geometries-set                   (into #{} geometries)]
    (remove-layers {:leaflet        leaflet
                    :geometries-set geometries-set
                    :geometries-map geometries-map})
    (add-new-shapes component-atm geometries)))

(defn- add-layers
  "Function that adds all the layers to the map"
  [leaflet layers]
  (doseq [{:keys [type url attribution]} layers]
    (let [_ (println type)
          layer (case type
                  :tile (js/L.tileLayer
                         url
                         (clj->js {:attribution attribution}))
                  :wms  (js/L.tileLayer.wms
                         url
                         (clj->js {:format      "image/png"
                                   :fillOpacity 1.0})))]
      (.addTo layer leaflet))))

(defn- add-map-controllers
  "Adds zoom and movement controllers to the map."
  [leaflet {:keys [zoom view]}]
  (.on leaflet "move" (fn [_]
                        (let [c (.getCenter leaflet)]
                          (reset! zoom (.getZoom leaflet))
                          (reset! view [(.-lat c) (.-lng c)]))))
  (add-watch view ::view-update
             (fn [_ _ old-view new-view]
               (when (not= old-view new-view)
                 (.setView leaflet (clj->js new-view) @zoom))))
  (add-watch zoom ::zoom-update
             (fn [_ _ old-zoom new-zoom]
               (when (not= old-zoom new-zoom)
                 (.setZoom leaflet new-zoom)))))

(defn- mount-leaflet-map
  "Initialize LeafletJS map for a newly mounted map component."
  [data-atm]
  (let [{:keys [layers id geometries
                view zoom on-click]} (:mapspec (rg/state data-atm))
        leaflet                      (js/L.map id)]
    (.setView leaflet (clj->js @view) @zoom)
    (add-layers leaflet layers)
    (rg/set-state data-atm {:leaflet        leaflet
                            :geometries-map {}})
    (when on-click
      (.on leaflet "click" (fn [e]
                             (on-click [(-> e .-latlng .-lat)
                                        (-> e .-latlng .-lng)])))) ;;TODO Cuando se pincha el mapa
    (add-map-controllers leaflet {:zoom zoom
                                  :view view})
    ;; If the mapspec has an atom containing geometries, add watcher
    ;; so that we update all LeafletJS objects
    (when geometries
      (add-watch geometries ::geometries-update
                 (fn [_ _ _ new-geometries]
                   (update-leaflet-geometries data-atm new-geometries))))))

(defn- update-leaflet-map [data-atm]
  (update-leaflet-geometries data-atm (-> data-atm rg/state
                                          :mapspec :geometries
                                          deref)))

(defn- leaflet-render
  "Render function for a leaflet map."
  [data-atm]
  (let [{map-id     :id
         map-width  :width
         map-height :heigth} (-> data-atm rg/state :mapspec)]
    [:div {:id    map-id
           :style {:width  map-width
                   :height map-height}}]))

(defn leaflet
  "A LeafletJS map component."
  [mapspec]
  (rg/create-class
   {:get-initial-state     (fn [_] {:mapspec mapspec})
    :component-did-mount   mount-leaflet-map
    :component-will-update update-leaflet-map
    :render                leaflet-render}))
