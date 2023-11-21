(ns fungus-friends.events
  (:require
   [clojure.string :refer [lower-case]]
   [re-frame.core :as rf]))


(defn prop-int->value
  "Takes the properties of a mushroom and returns the string value."
  [catalogue fung-prop]
  (lower-case
   (:label
    (first
     (filter #(= fung-prop (:value %)) catalogue)))))

(defn color->color-hex
  "Takes the color value of a mushroom and returns the hex color."
  [catalogue-color]
  (case catalogue-color
    "yellow" "#DAA21B"
    "red"    "#D0376C"
    "blue"   "#6093DB"
    "#68DAC0"))

(rf/reg-event-db
 ::set-catalogue
 (fn [db [_ kw catalogue]]
   (assoc-in db [:catalogue kw] catalogue)))

(rf/reg-event-db
 ::add-geometries
 (fn [db [_ _]]
   (let [{color-f :color
          spot-f  :spot}     (:filters db)
         mushrooms           (:all-mushrooms db)
         all-spots           (get-in db [:catalogue :spots])
         all-colors          (get-in db [:catalogue :color])
         filter-fn           (fn [mushroom]
                               (and
                                (if color-f
                                  (= color-f (:color mushroom))
                                  true)
                                (if spot-f
                                  (= spot-f (:spots mushroom))
                                  true)))
         displayed-mushrooms (filterv filter-fn mushrooms)
         str-description     (fn [{:keys [color name spots]}]
                               (str "The fungus " name " has "
                                    (prop-int->value all-spots spots)
                                    " spots and is color "
                                    (prop-int->value all-colors color) "."))
         new-geometries      (into []
                                   (for [{:keys [latlng color]
                                          :as   mushroom} displayed-mushrooms]
                                     {:type        :point
                                      :coordinates [(or latlng
                                                        [65.3 25.0])]
                                      :popup-msg   (str-description mushroom)
                                      :radius      10
                                      :color       (color->color-hex
                                                    (prop-int->value all-colors color))}))]
     (assoc db
            :display-mushrooms displayed-mushrooms
            :geometries new-geometries))))

(rf/reg-event-fx
 ::set-mushroom-filter
 (fn [{db :db} [_ data]]
   {:db       (assoc db :filters data)
    :dispatch [::add-geometries]}))
