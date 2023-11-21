(ns events
  (:require
   ["./front-end-api" :as fungus-api]
   [re-frame.core :as rf]))

(defn parse-mushroom-data [raw-data]
  (let [data (js->clj raw-data)]
    (mapv (fn [unparsed-map]
            (reduce (fn [m [kw val]]
                      (assoc m (keyword kw) val))
                    {} unparsed-map))
          data)))

(rf/reg-event-db
 ::set-mushrooms-data
 (fn [db [_ raw-data]]
   (let [mushrooms (parse-mushroom-data raw-data)]
     (assoc db :all-mushrooms mushrooms))))

#_(def geometries (atom [{:type        :polygon ;; Las figuras a pintar, poner hongos aqui
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
                          :popup-msg   "Icon"}]))

(rf/reg-event-fx
 ::initialize-app
 (fn [{db :db} _]
   (let [_ 1]
     {:db (assoc db :fungus nil
                 :geometries [{:type        :polygon
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
                              #_{:type        :icon
                                 :icon-url    "/img/dimitri.jpg"
                                 :coordinates [[65.1 25.2]]
                                 :popup-msg   "Icon"}
                              {:type        :point
                               :coordinates [[65.1 25.2]]
                               :popup-msg   "Point"
                               :color       "#EB1515"
                               }]
                 :position [52.0820, 5.2361]
                 :zoom-level 12)})))
