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

## Locking cursor to window

In some situations it might be necessary to lock the mouse cursor within the game window.
This can be useful to avoid triggering OS taskbar (or dock on MacOs).
The window supports a cursor lock that moves the cursor back towards center of the screen when it comes near the
border of the window.
The padding can be adjusted in ranges from 2 to 64.
If the padding is to low the mechanism might fail due to skipped pixels when moved fast by the user.
Also make sure your application is allowed to change mouse cursor position.
On MacOs you will be asked to allow input control on first activation of this setting. 

``` java
// enable lock
window.enableCursorLock(32);

// will return 'true'
window.isCursorLockEnabled();

// disable lock
window.disableCursorLock();
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