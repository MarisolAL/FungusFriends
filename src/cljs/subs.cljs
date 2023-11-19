(ns subs
  (:require
   [re-frame.core :as rf]))

(rf/reg-sub ;; TODO Eliminar
 ::db
 (fn [db _]
   db))

(rf/reg-sub
 ::geometries
 (fn [db _]
   (:geometries db)))

(rf/reg-sub
 ::map-position
 (fn [db _]
   (:position db)))

(rf/reg-sub
 ::zoom-level
 (fn [db _]
   (:zoom-level db)))

(rf/reg-sub
 ::fungus
 (fn [db _]
   (:fungus db)))
