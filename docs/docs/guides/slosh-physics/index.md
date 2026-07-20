# Slosh physics

:::info about this guide
This guide will explain the slosh phyiscs system within the engine and how to use the pre packed components to create your own dynamic liquids like water or lava.
:::

To make use of the components below add slosh physics related systems using `environment.enableSloshPhysics()`.

## Setup a slosh volume

A fluid body can be created via adding a `SloshVolumeComponent` to an entity.
The most important property of any volume is the count of nodes used to create the surface.
This number massively influences the volumes behavior.
The node count cannot be changed once the volume is created.

You can also specify multiple properties to change the volume behavior:

- **retract** speed used to return to normal position
- **dampening** reduction of wave speed over time.
- **transmission** amount of wave height used to affect neighbour surface nodes

The volume won't be visible unless a `SloshVolumeRenderComponent` is added as well.
The `SloshVolumeRenderComponent` supports two colors for a gradient effect but also allows specifying a single color for filling the volumes polygon.
The `SloshPostProcessingComponent` can also add to the immersion by creating a special post processing filter that creates and under water effect.

## Creating waves

Volumes won't show any waves unless you are actively creating waves.

This can be done by adding a `SloshTurbulenceComponent`.
This will create random waves on the whole volumes surface.
Alternatively you can create waves by manually setting a wave height of one of the fluids nodes.
See example code:

``` java
// manually creating a wave at the fifth node
sloshVolumeEntity.get(SloshVolumeComponent.class).height[4] = 20;
```

This mechanism can be used for custom wave machines.
You can also create waves automatically with interaction of physics entities with slosh volumes.
To do so, just add a `SloshInteractionComponent` to any physics entity.
The entity will create waves when touching the surface.
The impact of vertical and horizontal movement can be specified individually.

## Floating on the surface

Physics entities can also float on the surface of the fluid.
To create this effect add a `FloatComponent` to the physics entity.
The component can be customized to drastically change the behavior with custom dive depth etc..
The `FloatComponent` will also apply friction on the floating entity.
To automatically adjust the sprite rotation of an floating entity add a `FloatRotationComponent` to the entity.
Note: This will only adjust the sprite and won't have any effects on the physics behavior.

![floating object](floating-object.png)

## Diving into the fluid

It is possible to let floating entities dive into the fluid when another physics object gets on top of it.
To create this effect, add a `DiveComponent` to the physics object.
You can also customize the maximum dive depth of the object.

![diving objects](diving-objects.png)

## Sound and particle effects

To add real interaction to the volume a `SloshEffectsComponent` can be added.
This component automatically adds sound and particle effects when physics objects interact with the volume.
The component can be customized with own thresholds, scheduler and assets.

![slosh effects](slosh-effects.png)

## Post processing

For a final touch the `SloshPostProcessingComponent` can be added and customized.
The component will activate a post processing filter on the fluid body and automatically distort the background
using the current wave heights.