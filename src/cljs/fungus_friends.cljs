(ns fungus-friends
  (:require
   [components.leaflet-map.views :as l-map]
   [re-frame.core :as rf]
   [reagent.core :as rg]
   [subs :as subs]))


(defn fungus-map []
  (fn []
    [:div {:style {:width  "400px"
                   :height "400px"}}
     [l-map/leaflet {:id     "kartta"
                     :width  "400px"
                     :height "400px"
                     :layers [{:type        :tile
                               :url         "https://tile.openstreetmap.org/{z}/{x}/{y}.png"
                               :attribution "&copy; <a href=\"http://www.openstreetmap.org/copyright\">OpenStreetMap</a>"}]}]]))

(defn page []
  [:div.main-container.title-message
   [:h1 {:class "dark-blue"} "Welcome Fungus Friends!"]
   [:p {:class "dark-blue"} "Here you have your fungus"]
   [:p "Selects here"]
   [fungus-map]])
