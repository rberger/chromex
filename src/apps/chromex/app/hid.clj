(ns chromex.app.hid
  "Use the chrome.hid API to interact with connected HID devices.
   This API provides access to HID operations from within the context of an app.
   Using this API, apps can function as drivers for hardware devices.

   Errors generated by this API are reported by setting
   'runtime.lastError' and executing the function's regular callback. The
   callback's regular parameters will be undefined in this case.

     * available since Chrome 38
     * https://developer.chrome.com/apps/hid"

  (:refer-clojure :only [defmacro defn apply declare meta let partial])
  (:require [chromex.wrapgen :refer [gen-wrap-helper]]
            [chromex.callgen :refer [gen-call-helper gen-tap-all-events-call]]))

(declare api-table)
(declare gen-call)

; -- functions --------------------------------------------------------------------------------------------------------------

(defmacro get-devices
  "Enumerate connected HID devices.

     |options| - The properties to search for on target devices.

   This function returns a core.async channel of type `promise-chan` which eventually receives a result value.
   Signature of the result value put on the channel is [devices] where:

     |devices| - https://developer.chrome.com/apps/hid#property-callback-devices.

   In case of an error the channel closes without receiving any value and relevant error object can be obtained via
   chromex.error/get-last-error.

   https://developer.chrome.com/apps/hid#method-getDevices."
  ([options] (gen-call :function ::get-devices &form options)))

(defmacro get-user-selected-devices
  "Presents a device picker to the user and returns 'HidDeviceInfo' objects for the devices selected. If the user cancels the
   picker devices will be empty. A user gesture is required for the dialog to display. Without a user gesture, the callback
   will run as though the user cancelled. If multiple filters are provided devices matching any filter will be displayed.

     |options| - Configuration of the device picker dialog box.

   This function returns a core.async channel of type `promise-chan` which eventually receives a result value.
   Signature of the result value put on the channel is [devices] where:

     |devices| - https://developer.chrome.com/apps/hid#property-callback-devices.

   In case of an error the channel closes without receiving any value and relevant error object can be obtained via
   chromex.error/get-last-error.

   https://developer.chrome.com/apps/hid#method-getUserSelectedDevices."
  ([options] (gen-call :function ::get-user-selected-devices &form options))
  ([] `(get-user-selected-devices :omit)))

(defmacro connect
  "Open a connection to an HID device for communication.

     |device-id| - The 'HidDeviceInfo.deviceId' of the device to open.

   This function returns a core.async channel of type `promise-chan` which eventually receives a result value.
   Signature of the result value put on the channel is [connection] where:

     |connection| - https://developer.chrome.com/apps/hid#property-callback-connection.

   In case of an error the channel closes without receiving any value and relevant error object can be obtained via
   chromex.error/get-last-error.

   https://developer.chrome.com/apps/hid#method-connect."
  ([device-id] (gen-call :function ::connect &form device-id)))

(defmacro disconnect
  "Disconnect from a device. Invoking operations on a device after calling this is safe but has no effect.

     |connection-id| - The connectionId returned by 'connect'.

   This function returns a core.async channel of type `promise-chan` which eventually receives a result value.
   Signature of the result value put on the channel is [].

   In case of an error the channel closes without receiving any value and relevant error object can be obtained via
   chromex.error/get-last-error.

   https://developer.chrome.com/apps/hid#method-disconnect."
  ([connection-id] (gen-call :function ::disconnect &form connection-id)))

(defmacro receive
  "Receive the next input report from the device.

     |connection-id| - The connectionId returned by 'connect'.

   This function returns a core.async channel of type `promise-chan` which eventually receives a result value.
   Signature of the result value put on the channel is [report-id data] where:

     |report-id| - The report ID or 0 if none.
     |data| - The report data, the report ID prefix (if present) is removed.

   In case of an error the channel closes without receiving any value and relevant error object can be obtained via
   chromex.error/get-last-error.

   https://developer.chrome.com/apps/hid#method-receive."
  ([connection-id] (gen-call :function ::receive &form connection-id)))

(defmacro send
  "Send an output report to the device.Note: Do not include a report ID prefix in data. It will be added if necessary.

     |connection-id| - The connectionId returned by 'connect'.
     |report-id| - The report ID to use, or 0 if none.
     |data| - The report data.

   This function returns a core.async channel of type `promise-chan` which eventually receives a result value.
   Signature of the result value put on the channel is [].

   In case of an error the channel closes without receiving any value and relevant error object can be obtained via
   chromex.error/get-last-error.

   https://developer.chrome.com/apps/hid#method-send."
  ([connection-id report-id data] (gen-call :function ::send &form connection-id report-id data)))

(defmacro receive-feature-report
  "Request a feature report from the device.

     |connection-id| - The connectionId returned by 'connect'.
     |report-id| - The report ID, or 0 if none.

   This function returns a core.async channel of type `promise-chan` which eventually receives a result value.
   Signature of the result value put on the channel is [data] where:

     |data| - The report data, including a report ID prefix if one is sent by the device.

   In case of an error the channel closes without receiving any value and relevant error object can be obtained via
   chromex.error/get-last-error.

   https://developer.chrome.com/apps/hid#method-receiveFeatureReport."
  ([connection-id report-id] (gen-call :function ::receive-feature-report &form connection-id report-id)))

(defmacro send-feature-report
  "Send a feature report to the device.Note: Do not include a report ID prefix in data. It will be added if necessary.

     |connection-id| - The connectionId returned by 'connect'.
     |report-id| - The report ID to use, or 0 if none.
     |data| - The report data.

   This function returns a core.async channel of type `promise-chan` which eventually receives a result value.
   Signature of the result value put on the channel is [].

   In case of an error the channel closes without receiving any value and relevant error object can be obtained via
   chromex.error/get-last-error.

   https://developer.chrome.com/apps/hid#method-sendFeatureReport."
  ([connection-id report-id data] (gen-call :function ::send-feature-report &form connection-id report-id data)))

; -- events -----------------------------------------------------------------------------------------------------------------
;
; docs: https://github.com/binaryage/chromex/#tapping-events

(defmacro tap-on-device-added-events
  "Event generated when a device is added to the system. Events are only broadcast to apps and extensions that have permission
   to access the device. Permission may have been granted at install time or when the user accepted an optional permission
   (see 'permissions.request').

   Events will be put on the |channel| with signature [::on-device-added [device]] where:

     |device| - https://developer.chrome.com/apps/hid#property-onDeviceAdded-device.

   Note: |args| will be passed as additional parameters into Chrome event's .addListener call.

   https://developer.chrome.com/apps/hid#event-onDeviceAdded."
  ([channel & args] (apply gen-call :event ::on-device-added &form channel args)))

(defmacro tap-on-device-removed-events
  "Event generated when a device is removed from the system. See 'onDeviceAdded' for which events are delivered.

   Events will be put on the |channel| with signature [::on-device-removed [device-id]] where:

     |device-id| - The deviceId property of the device passed to 'onDeviceAdded'.

   Note: |args| will be passed as additional parameters into Chrome event's .addListener call.

   https://developer.chrome.com/apps/hid#event-onDeviceRemoved."
  ([channel & args] (apply gen-call :event ::on-device-removed &form channel args)))

; -- convenience ------------------------------------------------------------------------------------------------------------

(defmacro tap-all-events
  "Taps all valid non-deprecated events in chromex.app.hid namespace."
  [chan]
  (gen-tap-all-events-call api-table (meta &form) chan))

; ---------------------------------------------------------------------------------------------------------------------------
; -- API TABLE --------------------------------------------------------------------------------------------------------------
; ---------------------------------------------------------------------------------------------------------------------------

(def api-table
  {:namespace "chrome.hid",
   :since "38",
   :functions
   [{:id ::get-devices,
     :name "getDevices",
     :callback? true,
     :params
     [{:name "options", :type "object"}
      {:name "callback",
       :type :callback,
       :callback {:params [{:name "devices", :type "[array-of-hid.HidDeviceInfos]"}]}}]}
    {:id ::get-user-selected-devices,
     :name "getUserSelectedDevices",
     :since "77",
     :callback? true,
     :params
     [{:name "options", :optional? true, :since "45", :type "object"}
      {:name "callback",
       :type :callback,
       :callback {:params [{:name "devices", :type "[array-of-hid.HidDeviceInfos]"}]}}]}
    {:id ::connect,
     :name "connect",
     :callback? true,
     :params
     [{:name "device-id", :type "integer"}
      {:name "callback", :type :callback, :callback {:params [{:name "connection", :type "object"}]}}]}
    {:id ::disconnect,
     :name "disconnect",
     :callback? true,
     :params [{:name "connection-id", :type "integer"} {:name "callback", :optional? true, :type :callback}]}
    {:id ::receive,
     :name "receive",
     :callback? true,
     :params
     [{:name "connection-id", :type "integer"}
      {:name "callback",
       :type :callback,
       :callback {:params [{:name "report-id", :type "integer"} {:name "data", :type "ArrayBuffer"}]}}]}
    {:id ::send,
     :name "send",
     :callback? true,
     :params
     [{:name "connection-id", :type "integer"}
      {:name "report-id", :type "integer"}
      {:name "data", :type "ArrayBuffer"}
      {:name "callback", :type :callback}]}
    {:id ::receive-feature-report,
     :name "receiveFeatureReport",
     :callback? true,
     :params
     [{:name "connection-id", :type "integer"}
      {:name "report-id", :type "integer"}
      {:name "callback", :type :callback, :callback {:params [{:name "data", :type "ArrayBuffer"}]}}]}
    {:id ::send-feature-report,
     :name "sendFeatureReport",
     :callback? true,
     :params
     [{:name "connection-id", :type "integer"}
      {:name "report-id", :type "integer"}
      {:name "data", :type "ArrayBuffer"}
      {:name "callback", :type :callback}]}],
   :events
   [{:id ::on-device-added, :name "onDeviceAdded", :since "41", :params [{:name "device", :type "hid.HidDeviceInfo"}]}
    {:id ::on-device-removed, :name "onDeviceRemoved", :since "41", :params [{:name "device-id", :type "integer"}]}]})

; -- helpers ----------------------------------------------------------------------------------------------------------------

; code generation for native API wrapper
(defmacro gen-wrap [kind item-id config & args]
  (apply gen-wrap-helper api-table kind item-id config args))

; code generation for API call-site
(def gen-call (partial gen-call-helper api-table))