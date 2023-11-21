(ns fungus-friends.subs
  (:require
   [re-frame.core :as rf]))

(rf/reg-sub
 ::new-geometries
 (fn [db _]
   (:geometries db)))
