(ns fungus-friends
  (:require
   ["leaflet" :as leaflet]
   [re-frame.core :as rf]
   [reagent.core :as rg]))

(defn create-map [map-id]
  (let [latitude  100
        longitude 300]
    (leaflet/map map-id #js{:center [latitude longitude] :zoom 12})))


(defn add-marker [map latitude longitude]
  (let [marker (leaflet/marker [latitude longitude])]
    (.addTo marker map)))


(defn map-fns []
  (let [map-container (js/document.getElementById "map-id")
        a             (-> js/document
                          (.getElementById "map-id"))
        _             (println "A is " a)
        latitude      150
        longitude     350
        _             (print leaflet/map leaflet/marker)
        _             (println "Map" map-container)]
    (if map-container
      (do
        (-> map-container
            (.appendChild (create-map "map-id")))
        (add-marker map-container latitude longitude))
      (println "Map container not found!"))))

(defn page []
  (let [_ (map-fns)]
    [:div.main-container.title-message
     [:h1 {:class "dark-blue"} "Welcome Fungus Friends!"]
     [:p {:class "purple"} "Here you have your fungus"]
     [:div {:id "map-id"
            :style {:height "400px"
                    :width "400px"
                    :background-color :gray}}]]))
