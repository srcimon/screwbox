# Window

The `engine.window()` module is used to control and retrieve information about the game window.

## Window State

The `Window` module provides methods for opening, closing and moving the game window on the screen.
Closing the window won't stop the game from running.
To actually quit the engine call `engine.stop()`.
There are also several function that provide information on the current window state.

``` java
window.close();
windows.isOpen(); // = false
window.close(); // does nothing, because already closed

window.moveTo(Offset.at(10, 40));
window.open(); // window opens at new position 10:40
window.hasFocus(); // will be false when the window runs in background
```

## Window title

The window title can also be set via `window.setTitle("My game")`.
The title will be initialized with the `engine.name()`.
Changing the window title is quite slow.
Constant refreshing of the window title should be avoided.

## Changing the cursor

The mouse cursor that is used while the cursor is on top of the window can be changed to a custom image, to be hidden or
to be the default cursor of the operation system ScrewBox is running on.

The window functions don't support animated cursors.
These can be archived by drawing an animated sprite at the cursor position.
The easiest way to do so is by using basic components.

``` java
environment.addEntity("animated cursor",
        new TransformComponent(),
        new CursorAttachmentComponent(),
        new RenderComponent(animatedSprite);
```

## Drag files on the window

The window will record drop events that can be pulled by any entity system.
So you can finally add your word documents inside your super mario clone.

``` java
engine.window().filesDroppedOnWindow().ifPresent(drop -> {
    String formatted = "dropped %s files at %s:%s".formatted(
        drop.files().size(),
        drop.offset().x(), 
        drop.offset().y());
        
    // will log "dopped 2 files at 10:52 dragging files on top of the window
    engine.log().info(formatted);
});
```