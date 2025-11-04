# Environment

The ScrewBox ECS implementation.
See [Entity System (ECS)](../fundamentals/ecs).
The current environment can be obtained using `engine.environment()`.

[Using scenes](scenes) creates multiple environments, one for every scene.

## Systems

The environment contains all currently active systems.
It provides multiple function for adding and removing entity systems.
An `EntitySystem` must be unique in the `Environment`.
Adding a second instance will result in an exception.

``` java
// adding all systems from custom package
environment.addSystemsFromPackage("io.github.myusername.mygame.systems");

// adding all prepacked systems
environment.enableAllFeatures();
```

### Execution order

All entity systems will be executed in their individual order.
This order can be set when adding the `EntitySystem` to `Environment` or by adding an `ExecutionOrder` annotation to the `EntitySystem`.
The annotation will assign the specified `Order` value to the `EntitySystem`.
The ordinal of the enum will define the execution order.
For most of the entity systems there will not be any need for an ordered execution,
but all systems that draw on the screen use this mechanism to draw in the current order.
E.g. the `LightRenderSystem` will draw the (dark areas) on top of the imaged drawn by the `RenderSystem`.

## Entities

The environment also stores all entities and their components.

:::warning
Entities added during an update cycle of an `EntitySystem` wont be available right away.
This is due to improving performance using a caching mechanism.
The next `EntitySystem` that is executed after the current one will the first one to obtain the `Entity`.
:::

``` java
// adding an entity
environment.addEntity(new Entity()
        .name("player")
        .add(new PhysicsComponent())
        .add(new TransformComponent(0, 0, 16, 16)));
```

### Name and id

Entities are mostly just containers for components.
Entities can also have a name.
The name is mostly for debugging purpose.
The id must be unique in the environment and can also be used to query for an `Entity`.

## Components

Components contain data that is processed by entity systems every single frame.
As a rule of thumb components are not supposed to contain any code.
Components should be serializable to allow [saving and loading the game state](#saving-and-loading-the-game-state).

It's no bad practice to add public properties to the components and not use getters and setters.

## Singletons

The environment also supports searching for singleton entities and components.


``` java
Archetype PLAYER = Archetype.of(PlayerComponent.class);

// will throw exception when no or more than one instance is found 
Entity player = engine.environment().fetchSingleton(PLAYER);
Component playerComponent = engine.environment().fetchSingletonComponent(PlayerComponent.class);

// more forgiving, will only throw exception on more than one instance
Optional<Entity> optionalPlayer = engine.environment().tryFetchSingleton(PLAYER);
Optional<Component> optionalPlayerComponent = engine.environment().tryFetchSingletonComponent(PlayerComponent.class);
```

## Archetypes

You can also define a so called `Archetype` as search query for entities.
Creating the search query costs some performance so using archetypes is the recommended way to search for entities.
Also this can enhance the readability of your source code.

``` java
public class DrawEntityOutlineSystem implements EntitySystem {

    private static final Archetype MOBILE = Archetype.of(TransformComponent.class);
            
    @Override
    public void update(Engine engine) {
        for(Entity entity : engine.environment().fetchAll(MOBILE)) {
            engine.graphics().world().drawRectangle(entity.bounds(), RectangleDrawOptions.outline(Color.RED));
        }
    }
}
```

## Saving and loading the game state

It's possible to save the current game state by exporting all entities (and their components) to the file system.
The resulting save game file will only contain the entities, not the currently attached entity systems.

``` java
// export entities to file
environment.saveToFile("mysavegame.sav");

// replace all entities with the ones from the savegame
environment.loadFromFile("mysavegame.sav");
```