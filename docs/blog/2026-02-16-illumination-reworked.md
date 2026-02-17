---
title: Illumination reworked
authors: [ srcimon ]
tags: [ new-features, milestones ]
---

Hello everyone,

the last three versions of ScrewBox made a huge effort to improve the game illumination:

The overall performance was improved significantly by improving the speed of the internally used image filters and
algorithms.
The blurring filter is now 20-30% faster and supports higher blur values for even smoother visuals.
The time consumed for image opacity inversion was reduced by a staggering 98%.

This increased throughput allows for higher-quality shadow maps and heavier shadow blurring without sacrificing frame
rates.

![occluders.png](../docs/guides/soft-physics/occluders.png)

I have also introduced a new directional light source and backdrop occluders:

- Directional light easily simulate natural illumination like sunlight.
- Backdrop occluders create shadows directly on the background behind objects.
  Backdrop occluders work in perfect harmony with soft physics entities.
  The shadows add a new level of immersion to these entities.

<!-- truncate -->