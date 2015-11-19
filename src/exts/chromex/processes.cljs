(ns chromex.processes (:require-macros [chromex.processes :refer [gen-wrap]])
    (:require [chromex-lib.core]))

; -- functions ------------------------------------------------------------------------------------------------------

(defn terminate* [config process-id]
  (gen-wrap :function ::terminate config process-id))

(defn get-process-id-for-tab* [config tab-id]
  (gen-wrap :function ::get-process-id-for-tab config tab-id))

(defn get-process-info* [config process-ids include-memory]
  (gen-wrap :function ::get-process-info config process-ids include-memory))

; -- events ---------------------------------------------------------------------------------------------------------

(defn on-updated* [config channel]
  (gen-wrap :event ::on-updated config channel))

(defn on-updated-with-memory* [config channel]
  (gen-wrap :event ::on-updated-with-memory config channel))

(defn on-created* [config channel]
  (gen-wrap :event ::on-created config channel))

(defn on-unresponsive* [config channel]
  (gen-wrap :event ::on-unresponsive config channel))

(defn on-exited* [config channel]
  (gen-wrap :event ::on-exited config channel))
