(ns dispatcher.core
  (:require [cljs.core.async :refer (put! chan mult tap <! >!)])
  (:require-macros [cljs.core.async.macros :refer (go-loop)]))




(def ^:private ch (chan))
(def ^:private actions-mult (mult ch))

(defn dispatch
  "Put the action in the actions channel"
  [data]
  (put! ch data))


(defn process
  "Gets a function, which will be called with the recieved actions.
  The function should return a channel with the result of process.
  Returns a channel, which will be filled with the processed actions."
  [<responder]
  (let [actions-ch (chan)
        done-ch    (chan)]
    (tap actions-mult actions-ch)
    (go-loop []
             (let [action (<! actions-ch)
                   result (<! (apply <responder action))]
               (>! done-ch [action result]))
             (recur))
    done-ch))
