(ns events
  (:require
   [re-frame.core :as rf]))

(rf/reg-event-fx
 ::initialize-app
 (fn [{db :db} _]
   {:db (assoc db :fungus nil
               :geometries [{:type        :polygon ;; Las figuras a pintar, poner hongos aqui
                             :coordinates [[65.1 25.2]
                                           [65.15 25.2]
                                           [65.125 25.3]]
                             :popup-msg   "Polygon"
                             :color       "#E3C2E5"}

                            {:type        :line
                             :coordinates [[65.3 25.0]
                                           [65.4 25.5]]
                             :popup-msg   "Line"
                             :color       "#431E70"}
                            {:type        :icon
                             :icon-url    "/img/dimitri.jpg"
                             :coordinates [[65.1 25.2]]
                             :popup-msg   "Icon"}]
               :position [65.1 25.2]
               :zoom-level 8)}))
