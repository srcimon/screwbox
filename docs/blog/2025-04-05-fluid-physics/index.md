---
title: Fluid physics
authors: [srcimon]
tags: [new-features]
---
Greetings!
Version 2.19.0 of the ScrewBox engine has just been released.
This version adds support for simple fluid physics.

![fluids](fluids.png)

To make something fluid, just add a `FluidComponent` to the entity you want it to be.
To make the fluid show up, add a `FluidRenderComponent` and pick your colors to make water, lava, or anything else you want.
Physics entities can swim in the fluid by adding a `FloatComponent`.

Future will tell what features will be added to the fluid physics.
In the current state, the fluids are quite rudimentary.
Combining the fluids with a reflection is definitely on my to-do list.
<!-- truncate -->