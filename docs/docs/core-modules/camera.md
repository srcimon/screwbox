# Camera

Screwbox uses a viewport concept.
Within the game there is at least one viewport that has individual camera control.
[Enabling split screen](../guides/split-screen) will create new viewports.
The camera of each viewport can be controlled individually.
To receive the current camera use `engine.graphics().camera()`.

## Automatic camera control

The simplest way to move the camera within the game world is to simply attach the camera to an entity.
This can be done by adding a `CameraTargetComponent` to the entity.
By changing the `viewportId` property you can select the target viewport for the camera.
This is only relevant when using split screen.

Don't forget to enable processing of the `CameraTargetComponent` by calling `environment.enableAllFeatures()`.

## Manual camera control

You can also obtain manual `Camera` controls using `engine.graphics().camera()`.
This allows some more specific controls like changing zoom or instant movement to a specified position.

:::info

Sadly you cannot set any zoom value.
This is due to zoom restriction which can also be changed via `Camera`,
but also pixel perfect mechanism that is in place to prevent graphic glitches.

:::

## Camera shake

`Camera` also allows setting of a short or infinite shake effect.
The method uses the specified `CameraShakeOptions` to apply the effect.
See example code:

``` java
camera.shake(CameraShakeOptions
    .lastingForDuration(Duration.oneSecond())
    .strength(4)
    .ease(Ease.SINE_IN_OUT)
    .swing(Rotation.degrees(10)));
```

The shake effect won't affect the position of the `Camera`.
To receive the actual position including the camera shake use `camera.focus()`.