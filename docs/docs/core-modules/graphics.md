# Graphics

The `Graphics` module provides access to all drawing related topics within the engine.
Because the `Graphics` module is the biggest and most powerful module it provides multiple sub modules for different graphic related tasks.

## Basics

This section describes the basic concepts and datatypes used when working with graphics in ScrewBox.

### Coordinates

The `Graphics` module uses a pixel-perfect coordinate system based on an `Offset`
, which represents the distance from the upper-left pixel on the screen, `Size`, which is used to describe the dimension
of any screen-related object, and `ScreenBounds`, which describes the combination of both, an area anywhere on the screen.

### Order of drawing tasks

The order of drawing tasks is set by the execution order of the `EntitySystem`.
Learn more about the execution order of entity systems in [Environment](environment.md#execution-order).
The one exception of this rule is when using a `SpriteBatch`.
The entries of the `SpriteBatch` have an individual order which determines the order of drawing.

### Sprites and Frames

By far the most important class when adding graphics to your game will be sprites.
A `Sprite` is a still or animated image.
The single images contained in a `Sprite` are frames.
Every frame can use an individual showing duration.
The sprite will be rendered as infinite loop of it's frames.
Any sprite will need at leas one `Frame`.
To create a sprite add one ore more resource images into your `src/main/resource` folder.

``` java
// create a sprite with a single image
Sprite player = Sprite.fromFile("player.png");

// create an animated sprite from a single image
Sprite playerWalking = Sprite.animatedAssetFromFile("player_walking.png", Size.square(16), Duration.ofMillis(100));
```

:::info
Sprites should only be created once and be reused when possible.
This will reduce cpu load an waiting times, especially when using shaders.
Background loading is also supported using [Assets](assets.md).
:::

## Canvas

Use the `Graphics.canvas()` to draw directly to the screen.
Every frame the `Canvas` will be cleared again.
So every drawing task has to be repeated in every frame.

The `Canvas` has a lot of distinct drawing methods available.
Most of this drawing methods use an option object that contains all drawing options for the specific task.
This limits the number of parameters for the drawing method.
These option classes are immutable and use a builder pattern.
See examples:

``` java
// will fill the whole canvas with red
canvas.fillWith(Color.RED);

// will draw a small half transparent white rectangle
canvas.drawRectangle(Offset.at(10, 20), Size.of(10,4), RectangleDrawOptions.filled(Color.WHITE.opacity(0.5));

// will draw the player sprite image using double size
canvas.drawSprite(player, Offset.at(100, 10), SpriteDrawOptions.scaled(2));
```

## World


## Screen
- You can use the `Graphics.screen()` to setup the actual drawing area on the game [Window](../window).

## Cameraa

Screwbox uses a viewport concept.
Within the game there is at least one viewport that has individual camera control.
[Enabling split screen](../guides/split-screen) will create new viewports.
The camera of each viewport can be controlled individually.
To receive the current camera use `engine.graphics().camera()`.

### Automatic camera control

The simplest way to move the camera within the game world is to simply attach the camera to an entity.
This can be done by adding a `CameraTargetComponent` to the entity.
By changing the `viewportId` property you can select the target viewport for the camera.
This is only relevant when using split screen.

Don't forget to enable processing of the `CameraTargetComponent` by calling `environment.enableAllFeatures()`.

### Manual camera control

You can also obtain manual `Camera` controls using `engine.graphics().camera()`.
This allows some more specific controls like changing zoom or instant movement to a specified position.

:::info

Sadly you cannot set any zoom value.
This is due to zoom restriction which can also be changed via `Camera`,
but also pixel perfect mechanism that is in place to prevent graphic glitches.

:::

### Camera shake

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

#TODO VIEWPORTS
#TODO Screen
#TODO COMPLETE THIS OVWERVIEW
#TODO rendercomponent entitysystem