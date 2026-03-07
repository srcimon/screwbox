---
title: Post-Processing
authors: [ srcimon ]
tags: [ new-features, milestones ]
---

Hi everyone,

version 3.24.0 introduces Post-Processing to ScrewBox.

What is Post-Processing?
Instead of rendering directly to the screen, the engine now renders the scene into a buffer first.
The render pipeline then apply on ore more visual filters to that image before it is presented at the actual screen.
The current release includes several built-in filters: `Shockwave`, `Wave`, `DeepSea`, `Underwater`, `FishEye`, `FacetEye`, `HeatHaze` and `Warp`

You can implement your own filters to modify the game's appearance.
While filters can be stacked, please note that some effects have a significant performance impact, which may limit the number of active layers depending on your hardware.

![post.png](post-processing.png)

The `Shockwave` Post-Processing filter functions differently than standard global filters.
These effects are anchored within the game world and account for camera movement, with radius and width changing over time.
To handle this complexity, ScrewBox provides a specialized API that allows you to trigger shockwaves with a single line of code.
The underlying filter is managed and applied internally on demand.

``` java
postProcessing.triggerShockwave(playerPosition, ShockwaveOptions.radius(40));
```

I'm planning to add some more support for such game world dependent effects in the future.
Also there are more post filters to come with the next versions.

Have a nice day!

<!-- truncate -->