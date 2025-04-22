# Dynamic fluids

:::info about this guide
This guide will explain the fluid system within the engine and how to use the pre packed components to create your own dynamic fluids.
:::


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


//TODO IMAGE

## Creating waves

//TODO DOCUMENT

## Floating on the surface

//TODO DOCUMENT
//TODO document roation

## Diving into the fluid

//TODO DOCUMENT