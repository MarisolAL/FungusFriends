(ns components.leaflet-map.events
  (:require
    [re-frame.core :as rf]))


(rf/reg-event-db
 ::update-map-position
 (fn [db [_ new-position]]
   (assoc db :position new-position)))

(rf/reg-event-db
 ::update-zoom
 (fn [db [_ new-zoom]]
   (assoc db :zoom new-zoom)))
