(ns core
  (:require
   ["./front-end-api" :as fungus-api]
   [events :as events]
   [fungus-friends.views :as ff]
   [re-frame.core :as rf]
   [reagent.dom :as rd]
   [reagent.core :as rg]
   [subs :as subs]))

(defn navbar []
  [:div.navbar__container
   [:div.nav-bar__content
    [:div.nav-button__logo-text-container
     [:img.nav-button__logo {:src "/img/logo.svg"
                             :alt "logo"}]
     [:span.nav-button__text "Fungus Friends"]]]])


(defn- hello-world []
  (rg/with-let [mushrooms-data (rg/atom nil)]
    (let [fn-p (fn []
                 (fungus-api/default)
                 (.then (fungus-api/default)
                        (fn [js-info]
                          (reset! mushrooms-data js-info))))
          _    (fn-p)
          _    (rf/dispatch [::events/set-mushrooms-data @mushrooms-data])]
      [:<>
       [navbar]
       [ff/page]])))


;; start is called by init and after code reloading finishes
(defn ^:dev/after-load start []
  (rd/render [hello-world] (js/document.getElementById "app")))



(defn init []
  ;; init is called ONCE when the page loads
  ;; this is called in the index.html and must be exported
  ;; so it is available even in :advanced release builds
  (js/console.log "init")
  (rf/dispatch-sync [::events/initialize-app])
  (start))

;; this is called before any code is reloaded
(defn ^:dev/before-load stop []
  (js/console.log "stop"))
