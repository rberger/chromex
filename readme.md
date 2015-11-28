# chromex [![Build Status](https://travis-ci.org/binaryage/chromex.svg)](https://travis-ci.org/binaryage/chromex)

[![Clojars Project](http://clojars.org/binaryage/chromex/latest-version.svg)](http://clojars.org/binaryage/chromex)

### Chrome Extension APIs wrapper library for ClojureScript.

[An **example project** demo-ing this library is here](https://github.com/binaryage/chromex-sample).

This library is auto-generated from [chromium sources](https://www.chromium.org/developers).

Currently Chromex provides following wrappers:

| API family | namespaces | properties | functions | events |
| --- | --- | --- | --- | --- |
| [Public Chrome Extension APIs](src/exts) | 82 | 44 | 354 | 185 |
| [Private Chrome Extension APIs](src/exts_private) | 45 | 3 | 315 | 67 |
| [Internal Chrome Extension APIs](src/exts_internal) | 16 | 0 | 89 | 15 |

Note: Chromex generator uses the same data source as the [developer.chrome.com/extensions/api_index](https://developer.chrome.com/extensions/api_index) web site.

This library is data-driven. Given API namespace, all API methods, properties and events are described in a Clojure map
along with their parameters, callbacks, versions and additional metadata ([a simple example - look for `api-table` here](src/exts/chromex/context_menus.clj)).
Chromex then provides a set of macros which consume this table and generate actual ClojureScript code wrapping native APIs.

These macros can be further parametrized which allows for greater flexibility. Sane defaults
are provided with following goals:

  * API version checking and deprecation warnings at compile-time
  * flexible marshalling of Javascript values to ClojureScript and back
  * callbacks are converted to core.async channels
  * events are emitted into core.async channels

#### API versions and deprecation warnings

Chrome Extension API is evolving. You might want to target multiple Chrome versions with slightly
different APIs. Good news is that our API data map contains full versioning and deprecation information.

By default you target the latest APIs. But you can target older API version instead and
we will warn you during compilation in case you were accessing any APIs not yet available in that particular version.

Additionally we are able to detect calls to deprecated APIs and warn you during compilation.

#### Flexible marshalling

Generated API data map contains information about all parameters and their types. Chromex provides a pluggable system
to specify how particular types are marshalled when crossing API boundary.

By default we marshall only a few types where it makes good sense. We don't want to blindly run all
parameters through `clj->js` and `js->clj` conversions. That could have unexpected consequences and maybe
performance implications. Instead we keep marshalling lean and give you an easy way how to provide your own macro which
can optionally generate required marshalling code (during compilation).

There is also a practical reason. This library is auto-generated and quite large - it would be too laborious to maintain
hairy marshalling conventions up-to-date with evolving Chrome API index. If you want to provide richer set of
marshalling for particular APIs you care about, you [can do that consistently](src/lib/chromex_lib/marshalling.clj).

#### Callbacks as core.async channels

Many Chrome API calls are async in nature and require you to specify a callback (for receiving an answer later).
You might want to watch this video explaining [API conventions](https://www.youtube.com/watch?v=bmxr75CV36A) in Chrome.

We automatically turn all API functions with a callback parameter to a ClojureScript function without the callback parameter
but returning a new core.async channel instead.

This mechanism is pluggable, so you can optionally implement your own mechanism of consuming callback calls.

#### Events are emitted into core.async channels

Chrome API namespaces usually provide multiple `event` objects, which you can subscribe with `.addListener`.
You provide a callback function which will get called with future events as they occur. Later you can call `.removeListener`
to unsubscribe from the event stream.

We think consuming events via core.async channel is more natural for ClojureScript developers.
In Chromex, you can request Chrome events to be emitted into a core.async channel provided by you.
And then implement a single loop to sequentially process events as they appear on the channel.

Again this mechanism is pluggable, so you can optionally implement a different mechanism for consuming event streams.

### Usage examples

We provide an example skeleton Chrome extension [chromex-sample](https://github.com/binaryage/chromex-sample). This project
acts as a code example but also as a skeleton with project configuration. We recommended to use it as starting point when
starting development of your own extension.

Please refer to [readme in chromex-sample](https://github.com/binaryage/chromex-sample) for further explanation and code examples.

### Advanced mode compilation

Chromex does not rely on externs file. Instead it is rigorously [using string names](https://github.com/clojure/clojurescript/wiki/Dependencies#using-string-names)
to access Javascript properties. I would recommend you to do the same in your own extension code. It is not that hard after all. You can use `oget`, `ocall` and `oapply`
macros from [`chromex-lib.support`](https://github.com/binaryage/chromex/blob/master/src/lib/chromex_lib/support.clj) namespace.

Note: There is a [chrome_extensions.js](https://github.com/google/closure-compiler/blob/master/contrib/externs/chrome_extensions.js) externs file available,
but that's been updated ad-hoc by the community. It is definitely incomplete and may be incorrect. But of course you are free to include the externs
file into your own project and rely on it if it works for your code. It depends on how recent/popular APIs are you going to use.

### Tapping events

Let's say for example you want to subscribe to [tab creation events](https://developer.chrome.com/extensions/tabs#event-onCreated) and
[web navigation "onCommited" events](https://developer.chrome.com/extensions/webNavigation#event-onCommitted).

```clojure
(ns your.project
  (:require [cljs.core.async :refer [chan close!]]
            [chromex.tabs :as tabs]
            [chromex.web-navigation :as web-navigation]
            [chromex-lib.chrome-event-channel :refer [make-chrome-event-channel]]))

(let [chrome-event-channel (make-chrome-event-channel (chan))]
  (tabs/tap-on-created-events chrome-event-channel)
  (web-navigation/tap-on-committed-events chrome-event-channel (clj->js {"url" [{"hostSuffix" "google.com"}]}))

  ; do something with the channel...
  (go-loop []
    (when-let [[event-id event-params] (<! chrome-event-channel)]
      (process-chrome-event event-id event-params)
      (recur))
    (println "leaving main event loop"))

  ; alternatively
  (close! chrome-event-channel)) ; this will unregister all chrome event listeners on the channel
```

As we wrote in previous sections, by default you consume Chrome events via core.async channels:

 1. first, you have to create/provide a channel of your liking
 2. then optionally wrap it in `make-chrome-event-channel` call
 3. then call one or more tap-<some>-events calls
 4. then you can process events as they appear on the channel

If you don't want to use the channel anymore, you should `close!` it.

Events coming from the channel are pairs `[event-id params]`, where params is a sequence of parameters passed into event's
callback function. See [chromex-sample](https://github.com/binaryage/chromex-sample/blob/master/src/background/chromex_sample/background/core.cljs)
for example usage. Refer to [Chrome's API docs](https://developer.chrome.com/extensions/api_index) for specific event objects.

Note: instead of calling tap-<some>-events you could call `tap-all-events`. This is a convenience function which will
tap events on all valid non-deprecated event objects in given namespace. For example `tabs/tap-all-events` will subscribe
to all existing tabs events in latest API version.

`make-chrome-event-channel` is a convenience wrapper for raw core.async channel. It is aware of event listeners and
is able to unsubscribe them when channel gets closed. But you are free to remove listeners manually as well,
tap calls return `ChromeEventSubscription` which gives you an interface to `unsubscribe!` given tap. This way you can
dynamically add/remove subscriptions on the channel.

Tap calls accept not only channel but also more optional arguments. These arguments will be passed into `.addListener` call
when registering Chrome event listener. This is needed for scenarios when event objects accept filters or other additional parameters.
`web-navigation/tap-on-committed-events` is [an example of such situation](https://developer.chrome.com/extensions/events#filtered).
Even more complex scenario is registering listeners on some [webRequest API events](https://developer.chrome.com/extensions/webRequest)
(see 'Registering event listeners' section).

#### Synchronous event listeners

TODO

### Similar projects

  * [suprematic/khroma](https://github.com/suprematic/khroma)

#### License [MIT](license.txt)
