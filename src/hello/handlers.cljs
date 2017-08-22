(ns hello.handlers
  (:require

    [re-frame.core :refer [reg-event-db ->interceptor after dispatch]]
    [clojure.spec.alpha :as s]
    [hello.db :as db :refer [app-db]]))

;; -- Interceptors ----------------------------------------------------------
;;
;; See https://github.com/Day8/re-frame/blob/develop/docs/Interceptors.md
;;
(defn check-and-throw
  "Throw an exception if db doesn't have a valid spec."
  [spec db]
  (when-not (s/valid? spec db)
    (let [explain-data (s/explain-data spec db)]
      (throw (ex-info (str "Spec check failed: " explain-data) explain-data)))))

(def validate-spec
  (if goog.DEBUG
    (->interceptor
        :id :validate-spec
        :after (fn [context]
                 (let [db (-> context :effects :db)]
                   (check-and-throw ::db/app-db db)
                   context)))
    ->interceptor))

;; -- Handlers --------------------------------------------------------------

(reg-event-db
  :initialize-db
  [validate-spec]
  (fn [_ _]
    app-db))

(reg-event-db
  :set-greeting
  [validate-spec]
  (fn [db [_ value]]
    (assoc db :greeting value)))


(reg-event-db
  :reset-routing-state
  validate-spec
  (fn [db _]
    (assoc db :routing (clj->js {:index  1,
                                 :routes [{:routes    [{:index     0,
                                                        :routes    [{:routeName "Home", :key "HomeInit"}],
                                                        :key       "HomeTab",
                                                        :routeName "HomeTab"}
                                                       {:index     0,
                                                        :routes    [{:routeName "Search", :key "SearchInit"}],
                                                        :key       "SearchTabInit",
                                                        :routeName "SearchTab"}],
                                           :index     0,
                                           :key       "InitTabs",
                                           :routeName "Tabs"}
                                          {:index     0,
                                           :routes    [{:routeName "Login1", :key "Login1Init"}],
                                           :key       "LoginStackInit",
                                           :routeName "LoginStack"}]}))))



(reg-event-db
  :login
  validate-spec
  (fn [db [_ value]]
    (js/setTimeout #(dispatch [:login-success]) 2000)
    db))

(reg-event-db
  :login-success
  validate-spec
  (fn [db [_ value]]
    (assoc db :routing (clj->js {:index  0,
                                 :routes [{:routes    [{:index     0,
                                                        :routes    [{:routeName "Home", :key "HomeInit"}],
                                                        :key       "HomeTab",
                                                        :routeName "HomeTab"}
                                                       {:index     0,
                                                        :routes    [{:routeName "Search", :key "SearchInit"}],
                                                        :key       "SearchTabInit",
                                                        :routeName "SearchTab"}],
                                           :index     0,
                                           :key       "TabsInit",
                                           :routeName "Tabs"}]}))))