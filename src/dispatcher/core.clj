(ns dispatcher.core
  (:require [schema.core :as s :include-macros true]))

(defmacro defaction
  "This defines a function that dispatches an action."
  [action doc & schemas]
  (let [args (for [s schemas] (gensym))]
    `(s/defn ^{:doc ~doc :always-validate true} ~action :- nil
       ~(vec (mapcat (fn [arg scma] [arg :- scma]) args schemas))
       (~'dispatcher.core/dispatch [~(keyword (name action)) ~@args]))))
