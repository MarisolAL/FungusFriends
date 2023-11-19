(ns components.leaflet-map.views
  (:require
   [components.leaflet-map.events :as events]
   [components.leaflet-map.subs :as subs]
   ["leaflet" :as leaflet]
   [re-frame.core :as rf]
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

(defmethod create-shape :icon [{:keys [coordinates icon-url]
                                :or   {icon-url "non-existent.jpg"}}]
  (let [icon (.icon js/L (clj->js {:iconUrl     icon-url
                                   :iconSize    [38 38]
                                   :iconAnchor  [22 94]
                                   :popupAnchor [(- 3) (- 76)]}))]
    (.marker js/L (clj->js (first coordinates)) #js {:icon icon})))

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
          (let [shape     (create-shape geom)
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
    (let [layer (case type
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
                          (rf/dispatch [::events/update-zoom (.getZoom leaflet)])
                          (rf/dispatch [::events/update-map-position [(.-lat c) (.-lng c)]]))))
  (add-watch view ::view-update
             (fn [_ _ old-view new-view]
               (when (not= old-view new-view)
                 (.setView leaflet (clj->js new-view) @zoom))))
  (add-watch zoom ::zoom-update
             (fn [_ _ old-zoom new-zoom]
               (when (not= old-zoom new-zoom)
                 (.setZoom leaflet new-zoom)))))

(defn mount-leaflet-map
  "Initialize LeafletJS map for a newly mounted map component."
  [js-data]
  (rg/with-let [zoom       (rf/subscribe [::subs/zoom-level])
                view       (rf/subscribe [::subs/map-position])
                geometries (rf/subscribe [::subs/geometries])]
    (let [{:keys [layers id]} (:map-spec (rg/state js-data))
          leaflet             (js/L.map id)]
      (.setView leaflet (clj->js @view) @zoom)
      (add-layers leaflet layers)
      (rg/set-state js-data {:leaflet        leaflet
                             :geometries-map {}})
      #_(when on-click
          (.on leaflet "click" (fn [e]
                                 (on-click [(-> e .-latlng .-lat)
                                            (-> e .-latlng .-lng)])))) ;;TODO Cuando se pincha el mapa
      (add-map-controllers leaflet {:zoom zoom
                                    :view view})
      ;; If the map-spec has an atom containing geometries, add watcher
      ;; so that we update all LeafletJS objects
      (when geometries
        (add-watch geometries ::geometries-update
                   (fn [_ _ _ new-geometries]
                     (update-leaflet-geometries js-data new-geometries)))))))

(defn update-leaflet-map [js-data]
  (rg/with-let [current-geometries (rf/subscribe [::subs/geometries])]
    (update-leaflet-geometries js-data @current-geometries)))

(defn leaflet-render
  "Render function for a leaflet map."
  [js-data]
  (let [{map-id     :id
         map-width  :width
         map-height :height} (-> js-data rg/state :map-spec)]
    [:div {:id    map-id
           :style {:width  map-width
                   :height map-height}}]))

(defn leaflet
  "A LeafletJS map component."
  [leaflet-map-spec]
  (rg/create-class
   {:get-initial-state     (fn [_] {:map-spec leaflet-map-spec})
    :component-did-mount   mount-leaflet-map
    :component-will-update update-leaflet-map ;; Change for did-update
    :render                leaflet-render}))
