---
title: Lighting reworked
authors: [srcimon]
tags: [new-features, milestones]
---

Hello everyone,

the last three versions of ScrewBox put huge effort to improve the game illumination:

The overall performance was improved significantly by improving the speed of the internally used image filters and algorithms.
The blurring filter got faster by 20-30% and can now supports higher blurring values.
The time consumed for image opacity inversion was reduced by 98%.

Better lighting esthetics can be archived by higher quality shadow maps and heavier shadow blurring.
This was made possible by the higher rendering throughput.

![occluders.png](../docs/guides/soft-physics/occluders.png)

New directional light source and backdrop occluders can be used in your scenes.
Directional light can be used as sunlight illumination.
Backdrop occluders create shadows directly on the background behind them.
Backdrop occluders go very well in sync with the soft physics entities.
Soft bodies and ropes can make pretty good use of the new shadow effects.

<!-- truncate -->