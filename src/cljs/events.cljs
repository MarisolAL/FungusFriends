(ns events
  (:require
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

(rf/reg-event-fx
 ::initialize-app
 (fn [{db :db} _]
   {:db (assoc db
               :fungus nil
               :position [52.0820, 5.2361]
               :zoom-level 16)}))
