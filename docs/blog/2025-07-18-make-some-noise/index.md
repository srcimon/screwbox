---
title: Make some noise
authors: [srcimon]
tags: [new-features]
---
Greetings!
Version 3.4.0 of ScrewBox added [Perlin Noise](https://en.wikipedia.org/wiki/Perlin_noise).
Perlin noise can be used to generate infinite deterministic landscape.
Version 3.6.0 of ScrewBox will support the generation of three dimensional.

![noise](noise.png)

To make something fluid, just add a `FluidComponent` to the entity you want it to be.
To make the fluid show up, add a `FluidRenderComponent` and pick your colors to make water, lava, or anything else you want.
Physics entities can swim in the fluid by adding a `FloatComponent`.


Future will tell what features will be added to the fluid physics.
In the current state, the fluids are quite rudimentary.
Combining the fluids with a reflection is definitely on my to-do list.
<!-- truncate -->