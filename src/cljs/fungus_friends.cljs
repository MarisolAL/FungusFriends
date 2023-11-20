(ns fungus-friends
  (:require
   ["./front-end-api" :as fungus-api]
   [clojure.string :as string]
   [components.button :refer [button]]
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

(defn- fungus-api->colors-opt
  "Transforms fungus-api data into a catalog for select component."
  [opt-key-name]
  (let [raw-data (opt-key-name (reduce (fn [m [kw val]]
                                         (assoc m
                                                (keyword kw) val))
                                       {}
                                       (js->clj fungus-api)))]
    (sort-by :value
             (reduce (fn [m [kw val]]
                       (let [number-val (js/parseInt kw) ]
                         (if (<=  0 number-val)
                           (conj m {:value number-val
                                    :label (string/capitalize val)})
                           m)))
                     []
                     raw-data))))

(defn page []
  (rg/with-let [color-atm (rg/atom nil)
                spot-atm (rg/atom nil)]
    (let [_ 1]
      [:div.main-container
       [:h1 {:class "dark-blue"} "Welcome Fungus Friends!"]
       [:p {:class "dark-blue"} "Here you have your fungus"]
       [:div.welcome-page__map-container
        [fungus-map]]
       [:div.welcome-page__selects-container
        [select {:label       "Spots"
                 :placeholder "Spots"
                 :options     (fungus-api->colors-opt :Spots)
                 :value       @spot-atm
                 :on-change   #(reset! spot-atm %)
                 :clearable   true}]
        [select {:label       "Color"
                 :placeholder "Color"
                 :options     (fungus-api->colors-opt :Color)
                 :value       @color-atm
                 :on-change   #(reset! color-atm %)
                 :clearable   true}]
        [button {:text     "Show mushrooms!"
                 :color    :primary
                 :on-click #(println "BUTTOn" @spot-atm @color-atm)}]]])))
