(ns fungus-friends
  (:require
   [components.leaflet-map.views :as l-map]
   [components.select :refer [select]]
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
  (rg/with-let [select-1-atm (rg/atom nil)
                select-2-atm (rg/atom nil)]
    [:div.main-container
     [:h1 {:class "dark-blue"} "Welcome Fungus Friends!"]
     [:p {:class "dark-blue"} "Here you have your fungus"]
     [:div.welcome-page__map-container
      [fungus-map]]
     [:div.welcome-page__selects-container
      [select {:label       "Select 1"
               :placeholder "Select"
               :options     [{:label "Option 1"
                              :value :option-1}
                             {:label "Option 2"
                              :value :option-2}]
               :value       @select-1-atm
               :on-change   #(reset! select-1-atm %)
               :clearable   true}]
      [select {:label       "Select 2"
               :placeholder "Select"
               :options     [{:label "Option 1"
                              :value :option-1}
                             {:label "Option 2"
                              :value :option-2}]
               :value       @select-2-atm
               :on-change   #(reset! select-2-atm %)
               :clearable   true}]]]))
