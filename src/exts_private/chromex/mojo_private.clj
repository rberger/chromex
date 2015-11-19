(ns chromex.mojo-private
  "The chrome.mojoPrivate API provides access to the mojo modules.
   
     * available since Chrome 42
     * https://developer.chrome.com/extensions/mojoPrivate"

  (:refer-clojure :only [defmacro defn apply declare meta let])
  (:require [chromex-lib.apigen :refer [gen-wrap-from-table]]
            [chromex-lib.callgen :refer [gen-call-from-table gen-tap-all-call]]
            [chromex-lib.config :refer [get-static-config gen-active-config]]))

(declare api-table)
(declare gen-call)

; -- functions ------------------------------------------------------------------------------------------------------

(defmacro define
  "Defines a AMD module.
   
     |factory| - |modules| is an array of the values returned by |dependencies| factories."
  [module-name dependencies #_factory]
  (gen-call :function ::define (meta &form) module-name dependencies))

(defmacro require-async
  "Returns a promise that will resolve to an asynchronously loaded module."
  [name]
  (gen-call :function ::require-async (meta &form) name))

; -- convenience ----------------------------------------------------------------------------------------------------

(defmacro tap-all [chan]
  (let [static-config (get-static-config)
        config (gen-active-config static-config)]
    (gen-tap-all-call static-config api-table (meta &form) config chan)))

; -------------------------------------------------------------------------------------------------------------------
; -- API TABLE ------------------------------------------------------------------------------------------------------
; -------------------------------------------------------------------------------------------------------------------

(def api-table
  {:namespace "chrome.mojoPrivate",
   :since "42",
   :functions
   [{:id ::define,
     :name "define",
     :callback? true,
     :params
     [{:name "module-name", :type "string"}
      {:name "dependencies", :type "[array-of-strings]"}
      {:name "factory", :type :callback, :callback {:params [{:name "modules", :type "[array-of-anys]"}]}}]}
    {:id ::require-async, :name "requireAsync", :return-type "any", :params [{:name "name", :type "string"}]}]})

; -- helpers --------------------------------------------------------------------------------------------------------

; code generation for native API wrapper
(defmacro gen-wrap [kind item-id config & args]
  (let [static-config (get-static-config)]
    (apply gen-wrap-from-table static-config api-table kind item-id config args)))

; code generation for API call-site
(defn gen-call [kind item src-info & args]
  (let [static-config (get-static-config)
        config (gen-active-config static-config)]
    (apply gen-call-from-table static-config api-table kind item src-info config args)))