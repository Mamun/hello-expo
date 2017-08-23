(ns hello.core
  (:require [reagent.core :as r :refer [atom]]
            [re-frame.core :refer [subscribe dispatch dispatch-sync]]
            [hello.handlers]
            [hello.subs]
            [hello.screen.login :as login]
            [hello.screen.home :as h]
            [hello.react-requires :refer [Image Platform Button TouchableOpacity Ionicons InteractionManager View ScrollView Text TouchableHighlight]]
    ;[cljs-react-navigation.reagent :refer [stack-navigator tab-navigator stack-screen tab-screen ] :as nav]
    ;[cljs-react-navigation.re-frame :refer [stack-navigator tab-navigator stack-screen tab-screen router] :as nav]
            [cljs-react-navigation.re-frame :refer [stack-navigator tab-navigator stack-screen tab-screen router] :as nav]
            ))

(def ReactNative (js/require "react-native"))

(def app-registry (.-AppRegistry ReactNative))
;(def text (r/adapt-react-class (.-Text ReactNative)))
;(def view (r/adapt-react-class (.-View ReactNative)))
;(def image (r/adapt-react-class (.-Image ReactNative)))
;(de ReactNavigation (js/require "react-navigation"))
;(def touchable-highlight (r/adapt-react-class (.-TouchableHighlight ReactNative)))
(def Alert (.-Alert ReactNative))

(defn alert [title]
  (.alert Alert title))



(def darkGrey "#484848")
(def blue "#348BFE")


(def AllRoutesStack (stack-navigator {:Tabs       {:screen h/Tabs}
                                      :Modal      {:screen (stack-screen h/placeholder {:title "Modal"})}
                                      :LoginStack {:screen login/LoginStack}}
                                     {:headerMode "none"
                                      :mode       "modal"}))


(defn app-root []
  (r/create-class
    (let [nav-state (subscribe [::nav/routing-state])]
      {:component-will-mount (fn []
                               (when-not @nav-state
                                 (dispatch-sync [:reset-routing-state])))
       :reagent-render       (fn []
                               [router {:root-router AllRoutesStack}])})))
#_(defn init []
    (.registerComponent AppRegistry "UIExplorer" #(r/reactify-component app-root)))

(defn init []
  (dispatch-sync [:initialize-db])
  (.registerComponent app-registry "main" #(r/reactify-component app-root)))


(comment

  ;(js/require "react-navigation")


  ;;Alert cvallback
  (let [v (.-Alert ReactNative)]
    (.alert v "Alert title " "Alrt body"
            (clj->js [{:text    "Ok"
                       :onPress (fn []
                                  (alert "Pressed")
                                  )
                       }])
            )
    )


  (alert "Check")

  (dispatch [:set-greeting "Hello From App"])

  )