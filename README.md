# dispatcher

A Clojurescript message dispatcher for a [flux](https://facebook.github.io/flux/) based architecture.

## Usage

First you need to define your actions. For this, use the `defaction` macro. `defaction` uses
[Prismatic/schema](https://github.com/Prismatic/schema) to define and validate the action.

```clojure
(defaction some-action
  "Some doc about the action."
  schema1 schema2 ... schemaN)
```

This will define a `some-action` function which will expect parameters matching against the provided
schemas. Calling the function is equal to dispatching the action. Function will return nil.

```clojure
(some-action value1 value2 ... valueN)
```

Then, you will need to register your callbacks with the dispatcher to be able to process actions.

```clojure
(defn <action-processor
  [action value1 value2 ... valueN]
  (go (do-something-with-the-values)))

(def processed-actions-chan
  (process <action-processor))
```

The `process` function registers the callback with the dispatcher. The callback will be called
with every recieved action, and it will need to return a channel which will eventually be filled
with the result of process. And `process` returns a channel that will contain the results of processed actions.
The shape of result is a vector with first element being the vector of action itself and secend element the
resolved result from the `<action-processor`.
