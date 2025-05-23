# Dynamic fluids

:::info about this guide
This guide will explain the fluid system within the engine and how to use the pre packed components to create your own dynamic fluids.
:::

To make use of the components below add fluid related systems using `environment.enableFluids()`.

## Setup a fluid body

A fluid body can be created via adding a `FluidComponent` to an entity.
The most important property of any fluid is the count of nodes used to create the surface.
This number massively influences the fluids behaviour.
The node count cannot be changed once the fluid is created.

You can also specify multiple properties to change the fluids behaviour:

- **retract** speed used to return to normal position
- **dampening** reduction of wave speed over time.
- **transmission** amount of wave height used to affect neighbour surface nodes

The fluid won't be visible unless a `FluidRenderComponent` is added as well.
The `FluidRenderComponent` supports two colors for a gradient effect but also allows specifying a single color for filling the fluid polygon.


## Creating waves

Fluids won't show any waves unless you are actively creating waves.

This can be done by adding a `FluidTurbulenceComponent`.
This will create random waves on the whole fluid surface.
Alternatively you can create waves by manually setting a wave height of one of the fluids nodes.
See example code:

``` java
// manually creating a wave at the fifth node
fluidEntity.get(FluidComponent.class).height[4] = 20;
```

This mechanism can be used for custom wave machines.
You can also create waves automatically with interaction of physics entities with fluids.
To do so, just add a `FluidInteractionComponent` to any physics entity.
The entity will create waves when touching the surface.
The impact of vertical and horizontal movement can be specified individually.


## Floating on the surface

Physics entities can also float on the surface of the fluid.
To create this effect add a `FloatComponent` to the physics entity.
The component can be customized to drastically change the behaviour with custom dive depth etc..
The `FloatComponent` will also apply friction on the floating entity.
To automatically adjust the sprite rotation of an floating entity add a `FloatRotationComponent` to the entity.
Note: This will only adjust the sprite and won't have any effects on the physics behaviour.

![floating object](floating-object.png)

## Diving into the fluid

It is possible to let floating entities dive into the fluid when another physics object gets on top of it.
To create this effect, add a `DiveComponent` to the physics object.
You can also customize the maximum dive depth of the object.

![diving objects](diving-objects.png)

## Sound and particle effects

To add real interaction to the fluid a `FluidEffectsComponent` can be added.
This component automatically adds sound and particle effects when physics objects interact with the fluid.
The component can be customized with own thresholds, scheduler and assets.

![fluid effects](fluid-effects.png)