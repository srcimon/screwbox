# Tweening

:::info about this guide
This guide will explain how to use tweening to animate any property of an entity.
:::

## How to add tweening

Tweening can be used to animate any property of an entity.
For some common use cases ScrewBox ships some tween components to make it somewhat easier to use tweens.
But if you are missing out on something you can always create your own tween related components.
It is highly recommended to use the central `TweenComponent` to get the current tween progress.
This will ensure you can mix in other tween related components as well.
But I am getting ahead of myself.
So how to create a tween animation?


The most important component to use tweening is the `TweenComponent` itself.
The `TweenComponent` will provide a value that is used by all other tween related components to control the animation.
For example, lets make an entity move between to positions.
To do so first create an entity with a tween component.
To make the tween loop indefinitely the `isLooped` property has to be set to `true`.

``` java
new Entity()
  .add(new TweenComponent(Duration.ofSeconds(2)), tween -> tween.isLooped = true)
```

The `TweenComponent` itself is just a source for the current tween progress and does not create visible change when added.
To add the actual movement add the `TweenPositionComponent` to the entity.
This component will make use of the current tween progress and add the motion animation.
See the full example:

``` java
public static void main(String[] args) {
    Engine engine = ScrewBox.createEngine();

    engine.environment()
        .enableAllFeatures()
        .addEntity(new Entity()
            .add(new RenderComponent(SpriteBundle.DOT_BLUE))
            .add(new TransformComponent(0, 0, 16, 16))
            .add(new TweenComponent(Duration.ofSeconds(2)), config -> config.isLooped = true)
            .add(new TweenPositionComponent(Vector.$(-80, 0), Vector.$(80, 0))));

    engine.start();
}
```

## Configuration options

The `TweenComponent` offers some pretty essential configuration options to configure the tween.
The most important ones are:

| property      | description                                                                                                     |
|---------------|-----------------------------------------------------------------------------------------------------------------|
| `duration`    | Duration of one animation cycle.                                                                                |
| `isLooped`    | Will the tween run in a loop or only once.                                                                      |
| `usePingPong` | Will the tween value run up and down again or will it start from zero.                                          |
| `ease`        | Configure some more exiting value changes than linear mootion. See [ease reference](../reference/ease/index.md) |

## Remove an entity after a timeout

If you are using a non looped tween, by adding the `TweenDestroyComponent` will delete the entire entity from the environment when the animation has ended.
This can be pretty handy to add a timeout on an entity.
This mechanism is also used by particles to remove them after a timeout.
