# Keyboard

Use keyboard input to control your game.
Obtain the `Keyboard` module using `engine.keyboard()`.

## Check for key status

The `Keyboard` provides multiple method for checking the status of a key.
Instead of the event driven AWT / Swing approach ScrewBox encourages using a polling mechanism.
This makes it easier to write thread safe code.

To check for key status use the desired method.

``` java
// will be true as long as the key is down
boolean isSpaceDown = keyboard.isDown(Key.SPACE);

// will be true only in the first frame when space is down
// must be released and pressed again to be true
boolean isSpacePressed = keyboard.isPressed(Key.SPACE);

// will only be true when both keys are down at the same time
final KeyCombination quit = KeyCombination.ofKeys(Key.SHIFT_LEFT, Key.Q);
boolean isQuit = keyboard.isDown(quit);
```

## Using aliases

Aliases can be used to make the key bindings configurable.
Also aliases may improve the readability of your source.
Any enum value may be an alias.


``` java title="1. define alias"
enum MyKeybindings {
    JUMP,
    LEFT,
    RIGHT
}
```

``` java title="2. set up alias binding"
keyboard.bindAlias(MyKeybindings.JUMP, Key.SPACE);
```

``` java title="3. using alias"
boolean isJump = keyboard.isDown(MyKeybindings.JUMP);
```

The alias binding can also be added by annotating the enum values with `@DefaultKey`.
The default value can be overwritten by using `keyboard.bindAlias(...)`.

``` java title="1. define alias"
enum MyKeybindings {
    
    @DefaultKey(Key.SPACE)
    JUMP,
    
    @DefaultKey(Key.ARROW_LEFT)
    LEFT,

    @DefaultKey(Key.ARROW_RIGHT)
    RIGHT
}
```

## Movement input

The keyboard also provides a very simple way to receive direction input for the arrow keys and the also very common wsad-combination:

``` java
// will be Vector.$(10,0) when pressing arrow right
Vector movement = keyboard.arrowKeysMovement(10);

// will be Vector.$(7.07, 7.07]) when pressing w and d at the same time
Vector movement = keyboard.wsadMovement(10);
```

## Recording text

To receive an input string from the keyboard the record mode must be started.

``` java
keyboard.recordedText(); // = Optional.empty()
keyboard.isRecording(); // = false

keyboard.startRecording();
keyboard.isRecording(); // = true

keyboard.recordedText(); // = Optional.of("")
// press a, b, c, backspace, d
keyboard.recordedText(); // = Optional.of("abd")
```