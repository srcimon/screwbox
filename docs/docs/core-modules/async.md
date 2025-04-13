# Async

Outsource cpu heavy tasks in separate threads.

## Run tasks in separate threads

The async module exposes some methods to support multithreading within ScrewBox applications.
Most of the things your applications will do within ScrewBox will be single threaded (with the exception
of [drawing on the screen](loop.md#game-loop-in-depth) and playing sounds).
This massively reduces the complexity and error proneness of any app build with ScrewBox.
But some tasks are really heavy and should be done in separate threads to avoid stuttering.
For those tasks the `Async` module is the recommended way to go.
When ScrewBox stops all threads spawned from the `Async` module will be stopped automatically.

``` java
// calling an async method from the asset module
// asnychronous methods always use the postfix Async
engine.assets().preparePackageAsync("dev.screwbox.demo");

// creating a custom action running in another thread
engine.async().run("my-task", () -> doCpuHeavyStuff());

// check if the task is still running
boolean isStillActive = engine.async().hasActiveTasks("my-task)";
```

### Context

All tasks created from the `Async` module use a context identifier.
A context can be anything that identifies the task that is running.
The context can be used to check if the task is still running (see example code above).
It can also be used to ensure only one task of a kind is running at a time.

``` java
// won't run task if another 'my-task' is still running
engine.async().runExclusive("my-task", () -> doCpuHeavyStuff());
```

:::info
Most of ScrewBox isn't thread safe.
All event handling from mouse and keyboard and window is actually moved from event driven push pattern to a pull pattern (`keyboard.isPressed(Key.SPACE)`).
This may cost some performance but will reduce threading related problems to a minimum.
:::

:::warning
But beware:
Concurrent modification exceptions will be raised if you add or remove  entities from an separate thread.
Using `Async` is only recommended to externalize some really cpu heavy tasks like pathfinding or creating images etc.
:::