(ns fungus-friends.views
  (:require
   ["./../front-end-api" :as fungus-api]
   [clojure.string :as string]
   [components.button :refer [button]]
   [components.leaflet-map.views :as l-map]
   [components.select :refer [select]]
   [fungus-friends.events :as events]
   [re-frame.core :as rf]
   [reagent.core :as rg]))


(defn fungus-map []
  (fn []
    [:div {:style {:width  "500px"
                   :height "500px"}}
     [l-map/leaflet {:id     "fungus-map"
                     :width  "500px"
                     :height "500px"
                     :layers [{:type        :tile
                               :url         "https://tile.openstreetmap.org/{z}/{x}/{y}.png"
                               :attribution "&copy; <a href=\"http://www.openstreetmap.org/copyright\">OpenStreetMap</a>"}]}]]))

(defn- fungus-api->opt
  "Transforms fungus-api data into a catalog for select component."
  [opt-key-name]
  (let [raw-data (opt-key-name (reduce (fn [m [kw val]]
                                         (assoc m
                                                (keyword kw) val))
                                       {}
                                       (js->clj fungus-api)))]
    (into []
          (sort-by :value
                   (reduce (fn [m [kw val]]
                             (let [number-val (js/parseInt kw)]
                               (if (<=  0 number-val)
                                 (conj m {:value number-val
                                          :label (string/capitalize val)})
                                 m)))
                           []
                           raw-data)))))

(defn page []
  (rg/with-let [color-atm (rg/atom nil)
                spot-atm  (rg/atom nil)
                spots-opt (fungus-api->opt :Spots)
                color-opt (fungus-api->opt :Color)
                _         (rf/dispatch [::events/set-catalogue :spots spots-opt])
                _         (rf/dispatch [::events/set-catalogue :color color-opt])]
    (fn [] ;; WIP CHECK refresh
      [:div.main-container
       [:h1 {:class "dark-blue"} "Welcome Fungus Friends!"]
       [:p {:class "dark-blue"} "Here you have your fungus"]
       [:div.welcome-page__map-container
        [fungus-map]]
       [:div.welcome-page__selects-container
        [select {:label       "Spots"
                 :placeholder "Spots"
                 :options     spots-opt
                 :value       @spot-atm
                 :on-change   #(reset! spot-atm %)
                 :clearable   true}]
        [select {:label       "Color"
                 :placeholder "Color"
                 :options     color-opt
                 :value       @color-atm
                 :on-change   #(reset! color-atm %)
                 :clearable   true}]
        [button {:text     "Show mushrooms!"
                 :color    :primary
                 :on-click #(rf/dispatch
                             [::events/set-mushroom-filter
                              {:color (:value @color-atm)
                               :spot  (:value @spot-atm)}])}]]])))
