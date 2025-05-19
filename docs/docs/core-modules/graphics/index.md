# Graphics

The `Graphics` module provides access to all drawing related topics within the engine.
It provides multiple sub modules for different graphic tasks.

## Basics

Before describing the submodules in depth please get to know the basic classes when working with graphics.

### Coordinates

The `Graphics` module uses a pixel-perfect coordinate system based on an `Offset`
, which represents the distance from the upper-left pixel on the screen, `Size`, which is used to describe the dimension
of any screen-related object, and `ScreenBounds`, which describes the combination of both, an area anywhere on the screen.

### Sprites and Frames

#TODO IMPLEMENT

## Submodules

### Canvas

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

# World


# Screen
- You can use the `Graphics.screen()` to setup the actual drawing area on the game [Window](../window).
- 
#TODO SPLITSCREEN
#TODO VIEWPORTS
#TODO Screen
#TODO COMPLETE THIS OVWERVIEW
#TODO rendercomponent entitysystem