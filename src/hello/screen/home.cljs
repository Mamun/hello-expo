(ns hello.screen.home
  (:require [reagent.core :as r :refer [atom]]
            [re-frame.core :refer [subscribe dispatch dispatch-sync]]
            [hello.handlers]
            [hello.subs]
            [hello.screen.login :as login]
            [hello.react-requires :refer [Image Platform Button TouchableOpacity Ionicons InteractionManager View ScrollView Text TouchableHighlight]]
    ;[cljs-react-navigation.reagent :refer [stack-navigator tab-navigator stack-screen tab-screen ] :as nav]
    ;[cljs-react-navigation.re-frame :refer [stack-navigator tab-navigator stack-screen tab-screen router] :as nav]
            [cljs-react-navigation.re-frame :refer [stack-navigator tab-navigator stack-screen tab-screen router] :as nav]
            ))

(def darkGrey "#484848")
(def blue "#348BFE")


(defn home
  "Each Screen will receive two props:
 - screenProps - Extra props passed down from the router (rarely used)
 - navigation  - The main navigation functions in a map as follows:
   {:state     - routing state for this screen
    :dispatch  - generic dispatch fn
    :goBack    - pop's the current screen off the stack
    :navigate  - most common way to navigate to the next screen
    :setParams - used to change the params for the current screen}"
  [props]
  (fn [{:keys [screenProps navigation] :as props}]
    (let [{:keys [navigate goBack]} navigation]
      [:> View {:style {:flex           1
                        :alignItems     "center"
                        :justifyContent "center"}}
       [:> Button {:style   {:fontSize 17}
                   :onPress #(navigate "Placeholder")
                   :title   "Modal Me!"}]
       [:> Button {:style   {:fontSize 17}
                   :onPress #(goBack)
                   :title   "Go Back!"}]
       [:> Button {:style   {:fontSize 17}
                   :onPress #(navigate "Placeholder")
                   :title   "Push Placeholder"}]
       [:> Button {:style   {:fontSize 17}
                   :onPress #(dispatch [:reset-routing-state])
                   :title   "Logout"}]])))

(defn search [props]
  (fn [{:keys [screenProps navigation] :as props}]
    (let [{:keys [state setParams]} navigation
          {:keys [params]} state]
      [:> View {:style {:flex           1
                        :alignItems     "center"
                        :justifyContent "center"}}
       [:> Text {} (str "Searching for " (:search params))]

       [:> Button {:style   {:fontSize 17}
                   :onPress #(setParams (clj->js {:search (rand-int 100)}))
                   :title   "Search!"}]])))

(defn placeholder [props]
  (fn [{:keys [screenProps navigation] :as props}]
    (let [{:keys [goBack]} navigation]
      [:> View {:style {:flex           1
                        :alignItems     "center"
                        :justifyContent "center"}}
       [:> Text {} "Nothing to see here!"]
       [:> Button {:style   {:fontSize 17}
                   :onPress #(goBack)
                   :title   "Go Back!"}]])))

(def home-tabBar {:tabBarLabel "Home"
                  :tabBarIcon  (fn [{:keys [tintColor focused] :as props}]
                                 [:> Ionicons {:name  (if focused "ios-home" "ios-home-outline")
                                               :color (if focused blue darkGrey)
                                               :size  26}])})

(def search-tabBar {:tabBarLabel "Search"
                    :tabBarIcon  (fn [{:keys [tintColor focused] :as props}]
                                   [:> Ionicons {:name  (if focused "ios-search" "ios-search-outline")
                                                 :color (if focused blue darkGrey)
                                                 :size  26}])})



(def HomeStack (stack-navigator {:Home        {:screen (stack-screen home {:title "Home"})}
                                 :Placeholder {:screen (stack-screen placeholder {:title "Placeholder"})}}))

(def SearchStack (stack-navigator {:Search {:screen (stack-screen search {:title "Search"})}}))

(def navbar-marginTop (if (= (aget Platform "OS") "android") 24 0))

(def Tabs (tab-navigator {:HomeTab   {:path   "home"
                                      :screen (tab-screen HomeStack home-tabBar)}
                          :SearchTab {:path   "search"
                                      :screen (tab-screen SearchStack search-tabBar)}}
                         {:tabBarOptions {:style {:marginTop navbar-marginTop}}}))

