---
sidebar_position: 3
---

# Entity System (ECS)

Learn how to handle game objects and their behaviour.

## Embedded ECS

ScrewBox uses the [entity component system architectural pattern (ECS)](https://en.wikipedia.org/wiki/Entity_component_system) to handle all game objects.
This ECS is named `Environment`.
An entity component system uses these three building blocks:

- **Entities** represent any object that lives inside the engine. E.g. a player, an enemy or even a wall.
  Entities contain components.
  Entities can also contain a unique id and an optional (non-unique) name.

- **Components** hold any data that is needed to process an entity by entity systems.

- **Entity systems** process the data contained in the components that entities hold.
  This happens every frame (around 120 times per second).

ScrewBox also supports ordered execution of the entity systems.
To order the execution of the entity system add an `@Order` annotation at the entity system class.

Learn more about the `Environment` in the [Environment component article](../core-modules/environment).

### Usage example

For better understanding what this actually means lets implement a simple `EntitySystem` that renders the outlines of
all entities.
To do so simply create a new class implementing the `EntitySystem` interface.

``` java
public class DrawEntityOutlineSystem implements EntitySystem {

    @Override
    public void update(Engine engine) {
       
    }
}
```

You can search for entities in the `Environment` that contain certain components.
We will use this to find all entities that have a `TransformComponent`.

``` java
engine.environment().fetchAllHaving(TransformComponent.class)
```

Then we use a drawing function to draw the outline of those entities.

``` java
public class DrawEntityOutlineSystem implements EntitySystem {

    @Override
    public void update(Engine engine) {
        for(Entity entity : engine.environment().fetchAllHaving(TransformComponent.class)) {
            engine.graphics().world().drawRectangle(entity.bounds(), RectangleDrawOptions.outline(Color.RED));
        }
    }
}
```

<details> 
<summary>To run this simply add your newly created `EntitySystem` to the `Environment`. </summary>

``` java
public class EcsDemo {

    public static void main(String[] args) {
        var demo = ScrewBox.createEngine();

        demo.environment()
                .addSystem(new DrawEntityOutlineSystem())
                .addEntity(new TransformComponent(10, 10, 100, 40))
                .addEntity(new TransformComponent(40, 50, 16, 16));

        demo.start();
    }
}
```

</details>

## Prepacked systems and components

ScrewBox comes with many prepacked systems and components.
See [components overview](../reference/components-overview.md).

You may have already experienced this in the example application of
the [Getting started guide](getting-started.md).
A lot of things that you can archive using the ScrewBox game engine can be done by simply adding the right components to
entities.
To do so you have to know about the different types of components and what entity systems will process these.

To make this easier the `Environment` can automatically add entity systems that can be used out of the box.
Simply call `environment.enableAllFeatures()` to add all prepacked entity systems to your game.
There are also more specific methods that reduce the number of entity systems added to your game.

A more detailed list of all components will be added to this documentation.
For now you can get an overview of all components in the
[JavaDoc](https://javadoc.io/doc/dev.screwbox/screwbox-core/latest/io/github/srcimon/screwbox/core/environment/package-summary.html).

## Final words on design decisions

- **public properties** You will note that all components prepacked with ScrewBox have public properties without getters
  and setters.
  This is a design decision due to reduce code overhead.

- **code within components** Some people really get upset upon adding code to components because it's against the
  design principle.
  ScrewBox was designed to have some fun and violates that principle on a few occasions.
  This is because it seamed the best solution for a problem at the time.