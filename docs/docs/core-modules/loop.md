# Loop

The game loop is responsible for updating all core modules.
The `engine.loop()` module provides access to current performance metrics and to controls the target frames per second.

## Frame rate (fps)

The game `Loop` tries to create a constant frame rate.
The default frame rate is 120 frames per second.
This means there are 120 updates of every `EntitySystem` in the `Environment` and the game screen is also rendered 120 times a second.
The frame rate can be changed to any other value above 120 fps.
Lower values are not possible.

``` java
// sets the new target fps
loop.setTargetFps(200);

// stops limiting the frame rate at all
// usefull for performance tests
loop.unlockFps();

// the actual current fps
final int fps = loop.fps();
```

:::info Why 120 fps?
ScrewBox uses no constant updates for the physics collision detection.
If there were too few updates of the physics system this would lead to glitches like falling through the floor etc.

Also not every frame that is rendered is actually shown on the monitor.
This is due to missing sync between the window that ScrewBox creates and the monitor.
:::

## Delta time

To keep the game at a constant speed independent from the actual fps the game loop provides a delta time value.
This value should be multiplied with anything that would otherwise create a non constant speed.

``` java
public class MovementSystem implements EntitySystem {
    
    @Override
    public void update(Engine engine) {
        for(final var entity : engine.environment().fetchAllHaving(TransformComponent.class)) {
            // ! entities would move faster on higher fps
            entity.moveBy(Vector.x(10)); 
            
            // constant movement
            entity.moveBy(Vector.x(engine.loop().delta() * 10)); 
            
            // same as above, but more compact
            entity.moveBy(Vector.x(engine.loop().delta(10))); 
        }
    }
}
```