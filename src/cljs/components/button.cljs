(ns components.button
  (:require
   [reagent.core :as rg]))


(def ^:private colors
  {:none      "btn__secondary"
   :primary   "btn__primary"
   :secondary "btn__secondary"
   :info      "btn__info"})


(defn button
  [{:keys [text color class]
    :as   props
    :or   {color :primary}}]
  (let [btn-class {:class ["btn" (color colors) class
                           (when (= color :secondary)
                             "btn--secondary")]}]
    [:button (merge btn-class
                    (dissoc props  :text :size :color :class))
     [:div.btn__content
      (when text
        [:span text])]]))
