# Common issues

:::info about this guide
This guide tries to help you with different issues that you might encounter.
If you are unable to solve your issue by yourself don't hesitate and get in [contact](https://screwbox.dev/impressum/)
with me
or create a [Github Issue](https://github.com/srcimon/screwbox/issues).
:::

## Bad performance

This might be one of the most common issues that you might encounter.
To debug performance it might be a good idea to enable fps logging by adding the `LogFpsSystem` to your `Environment`.
There can be a lot of reasons for bad performance:

- **Usage of suboptimal rendering API**
  The rendering API might not be detected correctly for your software / hardware setting.
  To fix this, try to set the rendering API explicitly in
  [this guide](../fundamentals/set-up-your-first-project/index.md#rendering-api).

- **Constant adding and removing of entities**
  Adding and removing entities and / or components can reduce performance of the entity component system dramatically
  by invalidating caches.
  Try to avoid adding and removing entities whenever you can.

- **Preparing shaders during run time**
  Rendering of shaders can be very slow.
  Good news is, that shaders use caches to reduce load on the system.
  To make best use of the shader caches, reuse same sprite whenever you can and prepare shaders on scene load.
  Learn more in [this guide](../core-modules/graphics/index.md#shaders).

- **Rendering many half transparent sprites**
  Rendering sprites comes at a cost.
  Rendering transparent sprites has a much higher impact on performance.
  Try to reduce count of drawing tasks to improve rendering performance.

## Error messages in console

Some users might experience similar error messages like the ones below.
Simply ignore these.
Sadly these cannot be avoided on MacOs.
[see](../fundamentals/set-up-your-first-project/index.md#error-messages-in-console)

``` text
2025-01-25 09:47:35.498 java[2300:79574] +[IMKClient subclass]: chose IMKClient_Modern
2025-01-25 09:47:35.498 java[2300:79574] +[IMKInputSession subclass]: chose IMKInputSession_Modern
```

## Metal Rendering issues

Metal rendering is currently affected by [JDK-8371679](https://bugs.java.com/bugdatabase/view_bug?bug_id=JDK-8371679)
which results in some weird pixel snapping effects on the screen edges.
To avoid these issues switch to OpenGl render.
Sadly this will come with a huge fps drop.

```java
Engine engine = ScrewBox.createEngine("My Game", RenderingApi.OPEN_GL);
```

## Components don't work

Some times adding a component to an entity does not have the expected effect.
There are two reasons that may cause this issue.

1. The entity system that processes the component has not been added.
   To solve this, check which entity system is missing and add it manually or simply use
   `environment.enableAllFeatures()`.
   The documentation may miss some hints on the corresponding system.
2. Another component may be needed for processing.
   Some components depend on other ones.
   If these ones are missing the entity does not get processed by the corresponding entity system.
   You can always have a sneak peek inside the entity system code and watch for the used archetype.
   These may hint the missing components.
   Also the JavaDoc on the component itself should hint on other components.
   If this documentation is missing I consider this a bug.
   Please report such shortcomings in the documentation, so I can fix them.