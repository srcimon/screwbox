# Mouse

Use `engine.mouse()` to receive mouse input for game control.

## Position

`Mouse` provides the pixel perfect position on the screen and the position in the game world.
This also works when using [split screen mode](../advanced-topics/split-screen).

``` java
// screen position, e.g. 402, 201
mouse.offset();

// game world position, e.g. 120.203, 503.20
mouse.position();

// false if mouse is not on screen
mouse.isCursorOnScreen();
```

## Button status

Different methods provide click status of the mouse buttons.
Drag will return the distance the mouse was moved since the last frame.

``` java
// true, if left mouse button is down
mouse.isDownLeft();

// only true, if button was not pressed in the last frame
mouse.isPressedLeft(MouseButton.LEFT); 

// distance moved since last frame
mouse.drag();

// count of scroll units
mouse.unitsScrolled();
```

## Using entity system

The `CursorAttachmentComponent` can be used to attach any entity to the mouse cursor position in the game world.

``` java
environment.addEntity(new Entity()
    .name("animated-cursort")
    .add(new TransformComponent())
    .add(new RenderComponent(animatedCursor))
    .add(new CursorAttachmentComponent()));
```