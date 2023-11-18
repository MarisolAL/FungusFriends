(ns components.leaflet-map.views
  (:require
   ;;["leaflet" :as leaflet]
   [reagent.core :as rg]))


(defmulti create-shape :type)

(defmethod create-shape :polygon [{:keys [coordinates]}]
  (js/L.polygon (clj->js coordinates)
                        #js {:color "red"
                             :fillOpacity 0.5}))

(defmethod create-shape :line [{:keys [coordinates]}]
  (js/L.polyline (clj->js coordinates)
                 #js {:color "blue"}))

(defmethod create-shape :point [{:keys [coordinates]}]
  (js/L.circle (clj->js (first coordinates))
               10
               #js {:color "green"}))

(defn- update-leaflet-geometries
  "Updates the Leaflet layers based on the data, mutates the LeafletJS map object."
  [component-atm geometries]
  (let [{:keys [leaflet geometries-map]} (rg/state component-atm)
        geometries-set                   (into #{} geometries)]
    ;; Remove all LeafletJS shape objects that are no longer in the new geometries
    (doseq [removed (keep (fn [[geom shape]]
                            (when-not (geometries-set geom)
                              shape))
                          geometries-map)]
      (.removeLayer leaflet removed))

    ;; Create new shapes for new geometries and update the geometries map
    (loop [new-geometries-map  {}
           [geom & geometries] geometries]
      (if-not geom
        ;; Update component state with the new geometries map
        (rg/set-state component-atm {:geometries-map new-geometries-map})
        (if-let [existing-shape (geometries-map geom)]
          ;; Have existing shape, don't need to do anything
          (recur (assoc new-geometries-map geom existing-shape) geometries)

          ;; No existing shape, create a new shape and add it to the map
          (let [shape (create-shape geom)]
            (.addTo shape leaflet)
            (recur (assoc new-geometries-map geom shape) geometries)))))))

(defn- mount-leaflet-map
  "Initialize LeafletJS map for a newly mounted map component."
  [data-atm]
  (let [{:keys [layers id geometries
                view zoom on-click]} (:mapspec (rg/state data-atm))
        leaflet                      (js/L.map id)
        _ (println @view)]
    (.setView leaflet (clj->js @view) @zoom)
    (doseq [{:keys [type url attribution]} layers] ;; Adds all the layers to the map
      (let [layer (case type
                    :tile (js/L.tileLayer
                           url
                           (clj->js {:attribution attribution}))
                    :wms  (js/L.tileLayer.wms
                           url
                           (clj->js {:format      "image/png"
                                     :fillOpacity 1.0})))]
        (.addTo layer leaflet)))
    (rg/set-state data-atm {:leaflet        leaflet
                            :geometries-map {}})

    ;; If mapspec defines callbacks, bind them to leaflet
    (when on-click
      (.on leaflet "click" (fn [e]
                             (on-click [(-> e .-latlng .-lat)
                                        (-> e .-latlng .-lng)])))) ;; Cuando se pincha el mapa

    ;; Add callback for leaflet pos/zoom changes
    ;; watcher for pos/zoom atoms
    (.on leaflet "move" (fn [e]
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
                   (.setZoom leaflet new-zoom))))
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

;;;;;;;;;;
;; Code to sync ClojureScript geometries vector data to LeafletJS
;; shape objects.






;;;;;;;;;
;; The LeafletJS Reagent component.

(defn leaflet
  "A LeafletJS map component."
  [mapspec]
  (rg/create-class
   {:get-initial-state     (fn [_] {:mapspec mapspec})
    :component-did-mount   mount-leaflet-map
    :component-will-update update-leaflet-map
    :render                leaflet-render}))
