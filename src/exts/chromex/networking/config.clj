(ns chromex.networking.config
  "Use the networking.config API to authenticate to captive
   portals.
   
     * available since Chrome 43
     * https://developer.chrome.com/extensions/networking.config"

  (:refer-clojure :only [defmacro defn apply declare meta let])
  (:require [chromex-lib.apigen :refer [gen-wrap-from-table]]
            [chromex-lib.callgen :refer [gen-call-from-table gen-tap-all-call]]
            [chromex-lib.config :refer [get-static-config gen-active-config]]))

(declare api-table)
(declare gen-call)

; -- functions ------------------------------------------------------------------------------------------------------

(defmacro set-network-filter
  "Allows an extension to define network filters for the networks it can handle. A call to this function will remove
   all filters previously installed by the extension before setting the new list.
   
     |networks| - Network filters to set. Every NetworkInfo must             either have the SSID or HexSSID
                  set. Other fields will be ignored.
     |callback| - Called back when this operation is finished."
  [networks #_callback]
  (gen-call :function ::set-network-filter (meta &form) networks))

(defmacro finish-authentication
  "Called by the extension to notify the network config API that it finished a captive portal authentication attempt
   and hand over the result of the attempt. This function must only be called with the GUID of the latest
   'onCaptivePortalDetected' event.
   
     |GUID| - Unique network identifier obtained from         'onCaptivePortalDetected'.
     |result| - The result of the authentication attempt.
     |callback| - Called back when this operation is finished."
  [guid result #_callback]
  (gen-call :function ::finish-authentication (meta &form) guid result))

; -- events ---------------------------------------------------------------------------------------------------------

(defmacro tap-on-captive-portal-detected
  "This event fires everytime a captive portal is detected on a network matching any of the currently registered
   network filters and the user consents to use the extension for authentication. Network filters may be set using the
   'setNetworkFilter'. Upon receiving this event the extension should start its authentication attempt with the
   captive portal. When the extension finishes its attempt, it must call 'finishAuthentication' with the GUID received
   with this event and the appropriate authentication result."
  [channel]
  (gen-call :event ::on-captive-portal-detected (meta &form) channel))

; -- convenience ----------------------------------------------------------------------------------------------------

(defmacro tap-all [chan]
  (let [static-config (get-static-config)
        config (gen-active-config static-config)]
    (gen-tap-all-call static-config api-table (meta &form) config chan)))

; -------------------------------------------------------------------------------------------------------------------
; -- API TABLE ------------------------------------------------------------------------------------------------------
; -------------------------------------------------------------------------------------------------------------------

(def api-table
  {:namespace "chrome.networking.config",
   :since "43",
   :functions
   [{:id ::set-network-filter,
     :name "setNetworkFilter",
     :callback? true,
     :params
     [{:name "networks", :type "[array-of-networking.config.NetworkInfos]"}
      {:name "callback", :type :callback}]}
    {:id ::finish-authentication,
     :name "finishAuthentication",
     :callback? true,
     :params
     [{:name "guid", :type "string"}
      {:name "result", :type "unknown-type"}
      {:name "callback", :type :callback}]}],
   :events
   [{:id ::on-captive-portal-detected,
     :name "onCaptivePortalDetected",
     :params [{:name "network-info", :type "networking.config.NetworkInfo"}]}]})

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