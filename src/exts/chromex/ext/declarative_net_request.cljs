(ns chromex.ext.declarative-net-request (:require-macros [chromex.ext.declarative-net-request :refer [gen-wrap]])
    (:require [chromex.core]))

; -- properties -------------------------------------------------------------------------------------------------------------

(defn max-number-of-rules* [config]
  (gen-wrap :property ::max-number-of-rules config))

(defn max-number-of-dynamic-rules* [config]
  (gen-wrap :property ::max-number-of-dynamic-rules config))

(defn getmatchedrules-quota-interval* [config]
  (gen-wrap :property ::getmatchedrules-quota-interval config))

(defn max-getmatchedrules-calls-per-interval* [config]
  (gen-wrap :property ::max-getmatchedrules-calls-per-interval config))

; -- functions --------------------------------------------------------------------------------------------------------------

(defn update-dynamic-rules* [config rule-ids-to-remove rules-to-add]
  (gen-wrap :function ::update-dynamic-rules config rule-ids-to-remove rules-to-add))

(defn get-dynamic-rules* [config]
  (gen-wrap :function ::get-dynamic-rules config))

(defn get-matched-rules* [config filter]
  (gen-wrap :function ::get-matched-rules config filter))

(defn set-action-count-as-badge-text* [config enable]
  (gen-wrap :function ::set-action-count-as-badge-text config enable))

; -- events -----------------------------------------------------------------------------------------------------------------

(defn on-rule-matched-debug* [config channel & args]
  (gen-wrap :event ::on-rule-matched-debug config channel args))

