(ns components.select
  (:require
   [clojure.string :as string]
   [goog.object :as gobj]
   ["react-select" :as rs]
   [reagent.core :as rg]))

(def ^:private react-select
  (rg/adapt-react-class (.-default rs)))

(def select-styles
  {"placeholder" (fn [provided-styles ^js/Object state]
                   (gobj/set provided-styles "whiteSpace" "nowrap")
                   (gobj/set provided-styles "textOverflow" "ellipsis")
                   provided-styles)

   "control"            (fn [provided-styles ^js/Object state]
                          (let [focused?     (. state -isFocused)
                                border-color (cond
                                               focused? "#E3C2E510"
                                               :else    "#EBEAED")
                                background   "#FBFAFC"]
                            ;; WARNING: `provided-styles` is mutated here
                            (js-delete provided-styles "boxShadow")
                            (js-delete provided-styles "&:hover")
                            (doseq [[k v] {"height"          42
                                           "outline"         "none"
                                           "borderRadius"    4
                                           "fontWeight"      400
                                           "fontSize"        14
                                           "borderColor"     border-color
                                           "backgroundColor" background}]
                              (gobj/set provided-styles k v))
                            provided-styles))
   "menuList"           (fn [provided-styles ^js/Object _]
                          ;; WARNING: `provided-styles` is mutated here
                          (gobj/set provided-styles "overflowX" "hidden")
                          (gobj/set provided-styles "textOverflow" "ellipsis")
                          provided-styles)
   "indicatorSeparator" (fn [_ _] nil)})

(defn- kw-value->str
  "Transforms a keyword into a string."
  [{:keys [value] :as m-option}]
  (if (keyword? value)
    (update m-option :value str)
    m-option))

(defn- kwstr-value->kw
  "Transforms a value from the react component into a keyword."
  [{:keys [value] :as m-option}]
  (if (and (string? value)
           (string/starts-with? value ":"))
    (update m-option :value #(-> % (subs 1) keyword))
    m-option))

(defn select-labels
  "Label component for selects."
  [{:keys [label]}
   select-component]
  [:label  {:style {:width "100%"}}
   (cond-> [:<>]
     label   (conj [:span.select__label label])
     :always (conj select-component))])

(defn select
  "react-select component."
  [{:keys [label options value on-change clearable placeholder]
    :as   properties}]
  (let [options-kw-safe (mapv kw-value->str options)]
    [select-labels {:label label}
     [react-select (merge {:styles       select-styles
                           :is-clearable clearable
                           :placeholder  placeholder
                           :on-focus     nil
                           :on-change    #(-> %
                                              (js->clj :keywordize-keys true)
                                              kwstr-value->kw
                                              on-change)
                           :value        (kw-value->str value)
                           :options      options-kw-safe}
                          (dissoc properties :searchable :clearable :left-icon :right-icon
                                  :l-inside-text :l-label :label :disabled :required
                                  :full-width :on-change :value :options))]]))
