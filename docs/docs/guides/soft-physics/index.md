# Soft physics

:::info about this guide
This guide will explain how to use the different soft physics features to create ropes etc..
:::

![rope.png](rope.png)

## Basic concept

To create soft bodies of any kind there are currently two main components that are used to link multiple entities
together:

- `SoftLinkComponent` will link the containing entity with another. The target is specified by id within the component.
  The component is used by other components to create an outline for soft bodies.
- `SoftStructureComponent` will basically do the same but with multiple targets.
  The component is used to create structural integrity when creating a soft body from multiple entities.

## Ropes

To create a rope you can create a chain of entities each linking to the next one using the `SoftLinkComponent`.
To render the rope add a `RopeComponent` and a `RopeRenderComponent` to the first entity within the chain.
The `RopeComponent` will build a comfortable list of entities contained within the rope.
The `RopeRenderComponent` will actually render a smoothed line between the entities from the entity list within the
`RopeComponent`.

:::note
Future versions will introduce apis for a more comfortable creation of ropes and other entity clusters.
:::

:::note
Future versions will introduce support for soft body structures and may be cloth.
:::