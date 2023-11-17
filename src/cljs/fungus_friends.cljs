(ns fungus-friends
  (:require
   [re-frame.core :as rf]
   [reagent.core :as rg]))

(defn page []
  [:div.main-container.title-message
   [:h1 {:class "dark-blue"} "Welcome Fungus Friends!"]
   [:p {:class "purple"} "Here you have your fungus"]])
