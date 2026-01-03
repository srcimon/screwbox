# Soft physics

:::info about this guide
This guide will explain how to use the different soft physics features to create ropes etc..
:::

## Basic concept

To create soft bodies of any kind there are currently two main components that are used to link multiple entities
together:

- `SoftLinkComponent` will link the containing entity with another. The target is specified by id within the component.
  The component is used by other components to create an outline for soft bodies.
- `SoftStructureComponent` will basically do the same but with multiple targets.
  The component is used to create structural integrity when creating a soft body from multiple entities.

## Ropes

![rope.png](rope.png)

To create a rope you can create a chain of entities each linking to the next one using the `SoftLinkComponent`.
To render the rope add a `RopeComponent` and a `RopeRenderComponent` to the first entity within the chain.
The `RopeComponent` will build a comfortable list of entities contained within the rope.
The `RopeRenderComponent` will actually render a smoothed line between the entities from the node list within the
`RopeComponent`.

The `SoftPhysicsSupport` helps with the rope creation.
The `createRope` method will create a linked list of prepared rope entities that you can customize further.
The first entity will be the one containing the `RopeComponent`.
The last entity will not have a `SoftLinkComponent`.

``` java
// creates 8 linked entities
List<Entity> rope = SoftPhysicsSupport.createRope($(4, 10), $(4, 50), 8, engine.environment());

// add rendering
rope.getFirst().add(new RopeRenderComponent(Color.MAGENTA, 2));

// customize the links flexibility
rope.forEach(node -> node.tryGet(SoftLinkComponent.class).ifPresent(link -> link.flexibility = 50));

// customize friction
rope.forEach(node -> node.get(PhysicsComponent.class).friction = 1);

// remove physics from the start node to make it fix
rope.getFirst().remove(PhysicsComponent.class);
```

## Soft Bodies

![bodies.png](bodies.png)

Creating soft bodies is similar to creating ropes.
Start by creating a loop of entities each linking to the next one using the `SoftLinkComponent`.
In contrast to building a rope the last element of the chain must link back to the first element.

The `SoftBodyComponent` will build a comfortable list of entities contained within the outline of the body.
This component is also required to use other functionality e.g. rendering and shape matching.
The entity containing the `SoftBodyComponent` will be referred as the soft body entity.

The `SoftBodyRenderComponent` will actually render a polygon created by the the entities from the node list within the
`SoftBodyComponent`.

### Preserving shape

To preserve the shape of the soft body you have created, add a `SoftStructureComponent` to some of the nodes and link
them to other ones.
This will add some basic structural integrity and works pretty well for some shapes.
If the soft body gets more complex the shape may quickly collapse in certain situations.
To preserve the original shape, simply add a `SoftBodyShapeComponent` to the soft body entity.
This component will add shape matching to the soft body which will instantly stabilize the original shape.
The component can be configured to disable rotation of the shape, if the goal is to keep the shape upright.

This image visualizes the outline links, the soft structure links and the links between the soft body and the shape
matching one.

![body-debug.png](body-debug.png)

### Soft body collisions

To add collisions between soft bodies add the `SoftBodyCollisionComponent` to all soft bodies that should collide
with each other.
Soft body collisions are far from perfect.
ScrewBox uses a mix of point in polygon and bisector ray collision preventions.
Collisions may add lots of momentum to the soft body.
Experiment with the different configuration properties of the `SoftStructureComponent`, `SoftLinkComponent` and the
`SoftBodyShapeComponent` to get the best results.

### Expand size

To expand a soft body add ad `SoftbodyPressureComponent` and specify the pressure value, that you want to apply.
Avoid applying very low negative values because this will mess up the body when the structural integrity is lower
than the pressure.