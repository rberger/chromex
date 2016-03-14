(ns chromex.app.easy-unlock-private
  "chrome.easyUnlockPrivate API that provides hooks to Chrome to
   be used by Easy Unlock component app.

     * available since Chrome 38"

  (:refer-clojure :only [defmacro defn apply declare meta let])
  (:require [chromex.wrapgen :refer [gen-wrap-from-table]]
            [chromex.callgen :refer [gen-call-from-table gen-tap-all-call]]
            [chromex.config :refer [get-static-config gen-active-config]]))

(declare api-table)
(declare gen-call)

; -- functions --------------------------------------------------------------------------------------------------------------

(defmacro get-strings
  "Gets localized strings required to render the API.

   This function returns a core.async channel which eventually receives a result value and closes.
   Signature of the result value put on the channel is [strings] where:

     |strings| - ?"
  ([] (gen-call :function ::get-strings &form)))

(defmacro generate-ec-p256-key-pair
  "Generates a ECDSA key pair for P256 curve. Public key will be in format recognized by secure wire transport protocol used
   by Easy Unlock app. Otherwise, the exact format for both key should should be considered obfuscated to the app. The app
   should not use them directly, but through this API.

   This function returns a core.async channel which eventually receives a result value and closes.
   Signature of the result value put on the channel is [public-key private-key] where:

     |public-key| - ?
     |private-key| - ?"
  ([] (gen-call :function ::generate-ec-p256-key-pair &form)))

(defmacro perform-ecdh-key-agreement
  "Given a private key and a public ECDSA key from different asymetric key pairs, it generates a symetric encryption key using
   EC Diffie-Hellman scheme.

     |private-key| - A private key generated by the app using     |generateEcP256KeyPair|.
     |public-key| - A public key that should be in the same format as the     public key generated by
                    |generateEcP256KeyPair|. Generally not the     one paired with |private_key|.

   This function returns a core.async channel which eventually receives a result value and closes.
   Signature of the result value put on the channel is [data] where:

     |data| - ?"
  ([private-key public-key] (gen-call :function ::perform-ecdh-key-agreement &form private-key public-key)))

(defmacro create-secure-message
  "Creates a secure, signed message in format used by Easy Unlock app to establish secure communication channel over unsecure
   connection.

     |payload| - The payload the create message should carry.
     |key| - The key used to sign the message content. If encryption algorithm     is set in |options| the same key will be
             used to encrypt the message.
     |options| - Additional (optional) parameters used to create the message.

   This function returns a core.async channel which eventually receives a result value and closes.
   Signature of the result value put on the channel is [data] where:

     |data| - ?"
  ([payload key options] (gen-call :function ::create-secure-message &form payload key options)))

(defmacro unwrap-secure-message
  "Authenticates and, if needed, decrypts a secure message. The message is in the same format as the one created by
   |createSecureMessage|.

     |secure-message| - The message to be unwrapped.
     |key| - Key to be used to authenticate the message sender. If encryption     algorithm is set in |options|, the same
             key will be used to decrypt     the message.
     |options| - Additional (optional) parameters used to unwrap the message.

   This function returns a core.async channel which eventually receives a result value and closes.
   Signature of the result value put on the channel is [data] where:

     |data| - ?"
  ([secure-message key options] (gen-call :function ::unwrap-secure-message &form secure-message key options)))

(defmacro seek-bluetooth-device-by-address
  "Connects to the SDP service on a device, given just the device's Bluetooth address. This function is useful as a faster
   alternative to Bluetooth discovery, when you already know the remote device's Bluetooth address. A successful call to this
   function has the side-effect of registering the device with the Bluetooth daemon, making it available for future outgoing
   connections.

     |device-address| - The Bluetooth address of the device to connect to.

   This function returns a core.async channel which eventually receives a result value and closes.
   Signature of the result value put on the channel is []."
  ([device-address] (gen-call :function ::seek-bluetooth-device-by-address &form device-address)))

(defmacro connect-to-bluetooth-service-insecurely
  "Connects the socket to a remote Bluetooth device over an insecure connection, i.e. a connection that requests no bonding
   and no man-in-the-middle protection. Other than the reduced security setting, behaves identically to the
   chrome.bluetoothSocket.connect() function.

     |socket-id| - The socket identifier, as issued by the     chrome.bluetoothSocket API.
     |device-address| - The Bluetooth address of the device to connect to.
     |uuid| - The UUID of the service to connect to.

   This function returns a core.async channel which eventually receives a result value and closes.
   Signature of the result value put on the channel is []."
  ([socket-id device-address uuid] (gen-call :function ::connect-to-bluetooth-service-insecurely &form socket-id device-address uuid)))

(defmacro update-screenlock-state
  "Updates the screenlock state to reflect the Easy Unlock app state.

     |state| - ?

   This function returns a core.async channel which eventually receives a result value and closes.
   Signature of the result value put on the channel is []."
  ([state] (gen-call :function ::update-screenlock-state &form state)))

(defmacro set-permit-access
  "Saves the permit record for the local device.

     |permit-access| - The permit record to be saved.

   This function returns a core.async channel which eventually receives a result value and closes.
   Signature of the result value put on the channel is []."
  ([permit-access] (gen-call :function ::set-permit-access &form permit-access)))

(defmacro get-permit-access
  "Gets the permit record for the local device.

   This function returns a core.async channel which eventually receives a result value and closes.
   Signature of the result value put on the channel is [permit-access] where:

     |permit-access| - ?"
  ([] (gen-call :function ::get-permit-access &form)))

(defmacro clear-permit-access
  "Clears the permit record for the local device.

   This function returns a core.async channel which eventually receives a result value and closes.
   Signature of the result value put on the channel is []."
  ([] (gen-call :function ::clear-permit-access &form)))

(defmacro set-remote-devices
  "Saves the remote device list.

     |devices| - The list of remote devices to be saved.

   This function returns a core.async channel which eventually receives a result value and closes.
   Signature of the result value put on the channel is []."
  ([devices] (gen-call :function ::set-remote-devices &form devices)))

(defmacro get-remote-devices
  "Gets the remote device list.

   This function returns a core.async channel which eventually receives a result value and closes.
   Signature of the result value put on the channel is [devices] where:

     |devices| - ?"
  ([] (gen-call :function ::get-remote-devices &form)))

(defmacro get-sign-in-challenge
  "Gets the sign-in challenge for the current user.

     |nonce| - Nonce that should be signed by the Chrome OS TPM. The signed     nonce is returned with the sign-in challenge.

   This function returns a core.async channel which eventually receives a result value and closes.
   Signature of the result value put on the channel is [challenge signed-nonce] where:

     |challenge| - The sign in challenge to be used when signing in the user     currently associated with the Easy Unlock
                   service.
     |signed-nonce| - Nonce signed by Chrome OS TPM, provided as an argument to     the |getSignInChallenge()| function and
                      signed by the TPM key     associated with the user."
  ([nonce] (gen-call :function ::get-sign-in-challenge &form nonce)))

(defmacro try-sign-in-secret
  "Tries to sign-in the current user with a secret obtained by decrypting the sign-in challenge. Check
   chrome.runtime.lastError for failures. Upon success, the user session will be started.

     |sign-in-secret| - ?

   This function returns a core.async channel which eventually receives a result value and closes.
   Signature of the result value put on the channel is []."
  ([sign-in-secret] (gen-call :function ::try-sign-in-secret &form sign-in-secret)))

(defmacro get-user-info
  "Retrieves information about the user associated with the Easy unlock service.

   This function returns a core.async channel which eventually receives a result value and closes.
   Signature of the result value put on the channel is [users] where:

     |users| - ?"
  ([] (gen-call :function ::get-user-info &form)))

(defmacro get-connection-info
  "Gets the connection info for the Bluetooth device identified by deviceAddress.

     |device-address| - ?

   This function returns a core.async channel which eventually receives a result value and closes.
   Signature of the result value put on the channel is [rssi transmit-power max-transmit-power] where:

     |rssi| - The received signal strength from the remote device in dB. |transmit_power| The local transmission power to the
              remote device in dB. |max_transmit_power| The maximum transmission power that can be achieved.
     |transmit-power| - ?
     |max-transmit-power| - ?"
  ([device-address] (gen-call :function ::get-connection-info &form device-address)))

(defmacro show-error-bubble
  "Shows an error bubble with the given |message|, anchored to an edge of the given |anchorRect| -- typically the right edge,
   but possibly a different edge if there is not space for the bubble to the right of the anchor rectangle. If the
   |link_range| is non-empty, renders the text within the |message| that is contained in the |link_range| as a link with the
   given |link_target| URL.

     |message| - ?
     |link-range| - ?
     |link-target| - ?
     |anchor-rect| - ?"
  ([message link-range link-target anchor-rect] (gen-call :function ::show-error-bubble &form message link-range link-target anchor-rect)))

(defmacro hide-error-bubble
  "Hides the currently visible error bubble, if there is one."
  ([] (gen-call :function ::hide-error-bubble &form)))

(defmacro set-auto-pairing-result
  "Sets the result of auto pairing triggered from onStartAutoPairing event. If auto pairing is completed successfully,
   |result.success| should be true so that Easy bootstrap flow would finish and starts the user session. Otherwise,
   |result.success| is set to false with an optional error message to be displayed to the user.

     |result| - ?

   This function returns a core.async channel which eventually receives a result value and closes.
   Signature of the result value put on the channel is []."
  ([result] (gen-call :function ::set-auto-pairing-result &form result)))

(defmacro find-setup-connection
  "Finds and connects the remote BLE device that is advertising: |setupServiceUUID|. Returns when a connection is found or
   |timeOut| seconds have elapsed.

     |setup-service-uuid| - ?
     |time-out| - ?

   This function returns a core.async channel which eventually receives a result value and closes.
   Signature of the result value put on the channel is [connection-id device-address] where:

     |connection-id| - The identifier of the connection found. To be used in future calls refering to this connection.
     |device-address| - The Bluetooth address of the remote device."
  ([setup-service-uuid time-out] (gen-call :function ::find-setup-connection &form setup-service-uuid time-out)))

(defmacro setup-connection-status
  "Returns the status of the connection with |connectionId|.

     |connection-id| - ?

   This function returns a core.async channel which eventually receives a result value and closes.
   Signature of the result value put on the channel is [status] where:

     |status| - The status of the connection with |connection_id|."
  ([connection-id] (gen-call :function ::setup-connection-status &form connection-id)))

(defmacro setup-connection-disconnect
  "Disconnects the connection with |connectionId|.

     |connection-id| - ?

   This function returns a core.async channel which eventually receives a result value and closes.
   Signature of the result value put on the channel is []."
  ([connection-id] (gen-call :function ::setup-connection-disconnect &form connection-id)))

(defmacro setup-connection-send
  "Sends |data| through the connection with |connnectionId|.

     |connection-id| - ?
     |data| - ?

   This function returns a core.async channel which eventually receives a result value and closes.
   Signature of the result value put on the channel is []."
  ([connection-id data] (gen-call :function ::setup-connection-send &form connection-id data)))

(defmacro setup-connection-get-device-address
  "Gets the Bluetooth address of the connection with |connectionId

     |connection-id| - ?

   This function returns a core.async channel which eventually receives a result value and closes.
   Signature of the result value put on the channel is [device-address] where:

     |device-address| - The bluetooth address of the connection with  |connectionId|."
  ([connection-id] (gen-call :function ::setup-connection-get-device-address &form connection-id)))

; -- events -----------------------------------------------------------------------------------------------------------------
;
; docs: https://github.com/binaryage/chromex/#tapping-events

(defmacro tap-on-user-info-updated-events
  "Event fired when the data for the user currently associated with Easy unlock service is updated. |userInfo| The updated
   user information.

   Events will be put on the |channel| with signature [::on-user-info-updated [user-info]] where:

     |user-info| - ?

   Note: |args| will be passed as additional parameters into Chrome event's .addListener call."
  ([channel & args] (apply gen-call :event ::on-user-info-updated &form channel args)))

(defmacro tap-on-start-auto-pairing-events
  "Event fired at the end of Easy bootstrap to start auto pairing so that a proper cryptohome key could be generated for the
   user.

   Events will be put on the |channel| with signature [::on-start-auto-pairing []].

   Note: |args| will be passed as additional parameters into Chrome event's .addListener call."
  ([channel & args] (apply gen-call :event ::on-start-auto-pairing &form channel args)))

(defmacro tap-on-connection-status-changed-events
  "Event fired when |connectionId| change status.

   Events will be put on the |channel| with signature [::on-connection-status-changed [connection-id old-status new-status]]
   where:

     |connection-id| - ?
     |old-status| - ?
     |new-status| - ?

   Note: |args| will be passed as additional parameters into Chrome event's .addListener call."
  ([channel & args] (apply gen-call :event ::on-connection-status-changed &form channel args)))

(defmacro tap-on-data-received-events
  "Event fired when |connectionId| receives |data|.

   Events will be put on the |channel| with signature [::on-data-received [connection-id data]] where:

     |connection-id| - ?
     |data| - ?

   Note: |args| will be passed as additional parameters into Chrome event's .addListener call."
  ([channel & args] (apply gen-call :event ::on-data-received &form channel args)))

(defmacro tap-on-send-completed-events
  "Event fired when |connectionId| sends |data|. |success| is true if the send operation was successful.

   Events will be put on the |channel| with signature [::on-send-completed [connection-id data success]] where:

     |connection-id| - ?
     |data| - ?
     |success| - ?

   Note: |args| will be passed as additional parameters into Chrome event's .addListener call."
  ([channel & args] (apply gen-call :event ::on-send-completed &form channel args)))

; -- convenience ------------------------------------------------------------------------------------------------------------

(defmacro tap-all-events
  "Taps all valid non-deprecated events in this namespace."
  [chan]
  (let [static-config (get-static-config)
        config (gen-active-config static-config)]
    (gen-tap-all-call static-config api-table (meta &form) config chan)))

; ---------------------------------------------------------------------------------------------------------------------------
; -- API TABLE --------------------------------------------------------------------------------------------------------------
; ---------------------------------------------------------------------------------------------------------------------------

(def api-table
  {:namespace "chrome.easyUnlockPrivate",
   :since "38",
   :functions
   [{:id ::get-strings,
     :name "getStrings",
     :callback? true,
     :params [{:name "callback", :type :callback, :callback {:params [{:name "strings", :type "object"}]}}]}
    {:id ::generate-ec-p256-key-pair,
     :name "generateEcP256KeyPair",
     :callback? true,
     :params
     [{:name "callback",
       :type :callback,
       :callback
       {:params
        [{:name "public-key", :optional? true, :type "ArrayBuffer"}
         {:name "private-key", :optional? true, :type "ArrayBuffer"}]}}]}
    {:id ::perform-ecdh-key-agreement,
     :name "performECDHKeyAgreement",
     :callback? true,
     :params
     [{:name "private-key", :type "ArrayBuffer"}
      {:name "public-key", :type "ArrayBuffer"}
      {:name "callback", :type :callback, :callback {:params [{:name "data", :optional? true, :type "ArrayBuffer"}]}}]}
    {:id ::create-secure-message,
     :name "createSecureMessage",
     :callback? true,
     :params
     [{:name "payload", :type "ArrayBuffer"}
      {:name "key", :type "ArrayBuffer"}
      {:name "options", :type "object"}
      {:name "callback", :type :callback, :callback {:params [{:name "data", :optional? true, :type "ArrayBuffer"}]}}]}
    {:id ::unwrap-secure-message,
     :name "unwrapSecureMessage",
     :callback? true,
     :params
     [{:name "secure-message", :type "ArrayBuffer"}
      {:name "key", :type "ArrayBuffer"}
      {:name "options", :type "object"}
      {:name "callback", :type :callback, :callback {:params [{:name "data", :optional? true, :type "ArrayBuffer"}]}}]}
    {:id ::seek-bluetooth-device-by-address,
     :name "seekBluetoothDeviceByAddress",
     :callback? true,
     :params [{:name "device-address", :type "string"} {:name "callback", :optional? true, :type :callback}]}
    {:id ::connect-to-bluetooth-service-insecurely,
     :name "connectToBluetoothServiceInsecurely",
     :callback? true,
     :params
     [{:name "socket-id", :type "integer"}
      {:name "device-address", :type "string"}
      {:name "uuid", :type "string"}
      {:name "callback", :type :callback}]}
    {:id ::update-screenlock-state,
     :name "updateScreenlockState",
     :callback? true,
     :params [{:name "state", :type "unknown-type"} {:name "callback", :optional? true, :type :callback}]}
    {:id ::set-permit-access,
     :name "setPermitAccess",
     :callback? true,
     :params
     [{:name "permit-access", :type "easyUnlockPrivate.PermitRecord"}
      {:name "callback", :optional? true, :type :callback}]}
    {:id ::get-permit-access,
     :name "getPermitAccess",
     :callback? true,
     :params
     [{:name "callback",
       :type :callback,
       :callback {:params [{:name "permit-access", :optional? true, :type "easyUnlockPrivate.PermitRecord"}]}}]}
    {:id ::clear-permit-access,
     :name "clearPermitAccess",
     :callback? true,
     :params [{:name "callback", :optional? true, :type :callback}]}
    {:id ::set-remote-devices,
     :name "setRemoteDevices",
     :callback? true,
     :params
     [{:name "devices", :type "[array-of-easyUnlockPrivate.Devices]"}
      {:name "callback", :optional? true, :type :callback}]}
    {:id ::get-remote-devices,
     :name "getRemoteDevices",
     :callback? true,
     :params
     [{:name "callback",
       :type :callback,
       :callback {:params [{:name "devices", :type "[array-of-easyUnlockPrivate.Devices]"}]}}]}
    {:id ::get-sign-in-challenge,
     :name "getSignInChallenge",
     :callback? true,
     :params
     [{:name "nonce", :type "ArrayBuffer"}
      {:name "callback",
       :type :callback,
       :callback
       {:params
        [{:name "challenge", :optional? true, :type "ArrayBuffer"}
         {:name "signed-nonce", :optional? true, :type "ArrayBuffer"}]}}]}
    {:id ::try-sign-in-secret,
     :name "trySignInSecret",
     :callback? true,
     :params [{:name "sign-in-secret", :type "ArrayBuffer"} {:name "callback", :type :callback}]}
    {:id ::get-user-info,
     :name "getUserInfo",
     :callback? true,
     :params
     [{:name "callback",
       :type :callback,
       :callback {:params [{:name "users", :type "[array-of-easyUnlockPrivate.UserInfos]"}]}}]}
    {:id ::get-connection-info,
     :name "getConnectionInfo",
     :callback? true,
     :params
     [{:name "device-address", :type "string"}
      {:name "callback",
       :type :callback,
       :callback
       {:params
        [{:name "rssi", :type "integer"}
         {:name "transmit-power", :type "integer"}
         {:name "max-transmit-power", :type "integer"}]}}]}
    {:id ::show-error-bubble,
     :name "showErrorBubble",
     :since "42",
     :params
     [{:name "message", :type "string"}
      {:name "link-range", :type "object"}
      {:name "link-target", :type "string"}
      {:name "anchor-rect", :type "object"}]}
    {:id ::hide-error-bubble, :name "hideErrorBubble", :since "43"}
    {:id ::set-auto-pairing-result,
     :name "setAutoPairingResult",
     :since "42",
     :callback? true,
     :params [{:name "result", :type "object"} {:name "callback", :optional? true, :type :callback}]}
    {:id ::find-setup-connection,
     :name "findSetupConnection",
     :since "47",
     :callback? true,
     :params
     [{:name "setup-service-uuid", :type "string"}
      {:name "time-out", :type "integer"}
      {:name "callback",
       :type :callback,
       :callback {:params [{:name "connection-id", :type "integer"} {:name "device-address", :type "string"}]}}]}
    {:id ::setup-connection-status,
     :name "setupConnectionStatus",
     :since "47",
     :callback? true,
     :params
     [{:name "connection-id", :type "integer"}
      {:name "callback",
       :type :callback,
       :callback {:params [{:name "status", :type "easyUnlockPrivate.ConnectionStatus"}]}}]}
    {:id ::setup-connection-disconnect,
     :name "setupConnectionDisconnect",
     :since "47",
     :callback? true,
     :params [{:name "connection-id", :type "integer"} {:name "callback", :optional? true, :type :callback}]}
    {:id ::setup-connection-send,
     :name "setupConnectionSend",
     :since "47",
     :callback? true,
     :params
     [{:name "connection-id", :type "integer"}
      {:name "data", :type "ArrayBuffer"}
      {:name "callback", :optional? true, :type :callback}]}
    {:id ::setup-connection-get-device-address,
     :name "setupConnectionGetDeviceAddress",
     :since "47",
     :callback? true,
     :params
     [{:name "connection-id", :type "integer"}
      {:name "callback", :type :callback, :callback {:params [{:name "device-address", :type "string"}]}}]}],
   :events
   [{:id ::on-user-info-updated,
     :name "onUserInfoUpdated",
     :params [{:name "user-info", :type "easyUnlockPrivate.UserInfo"}]}
    {:id ::on-start-auto-pairing, :name "onStartAutoPairing", :since "42"}
    {:id ::on-connection-status-changed,
     :name "onConnectionStatusChanged",
     :since "47",
     :params
     [{:name "connection-id", :type "integer"}
      {:name "old-status", :type "easyUnlockPrivate.ConnectionStatus"}
      {:name "new-status", :type "easyUnlockPrivate.ConnectionStatus"}]}
    {:id ::on-data-received,
     :name "onDataReceived",
     :since "47",
     :params [{:name "connection-id", :type "integer"} {:name "data", :type "ArrayBuffer"}]}
    {:id ::on-send-completed,
     :name "onSendCompleted",
     :since "47",
     :params
     [{:name "connection-id", :type "integer"}
      {:name "data", :type "ArrayBuffer"}
      {:name "success", :type "boolean"}]}]})

; -- helpers ----------------------------------------------------------------------------------------------------------------

; code generation for native API wrapper
(defmacro gen-wrap [kind item-id config & args]
  (let [static-config (get-static-config)]
    (apply gen-wrap-from-table static-config api-table kind item-id config args)))

; code generation for API call-site
(defn gen-call [kind item src-info & args]
  (let [static-config (get-static-config)
        config (gen-active-config static-config)]
    (apply gen-call-from-table static-config api-table kind item src-info config args)))