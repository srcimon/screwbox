---
title: Post-Processing
authors: [ srcimon ]
tags: [ new-features, milestones ]
---

Hi everyone,

it is now possible to use Post-Processing within ScrewBox games!

What is Post-Processing?
Instead of rendering directly to the screen, the engine now renders the scene into a buffer first.
The render pipeline then apply on ore more visual filters to that image before it presented the actual screen.
There are already some filters available in the current release: `Shockwave`, `Wave`, `DeepSea`, `Underwater`
Of course you can create your own filters.

Post-Processing filters can change the apperance of the game drastically.
Filters can be stacked even if this is quite limited due to the huge performance impact some of these filters can have.

![post.png](post-processing.png)

Shockwaves hold a unique position among these effects.
Unlike standard filters that apply to the entire screen, shockwaves should be created within the game world.
Also the waves change their radius and width over time.
Because of this complexity, ScrewBox provides a specialized API for them.
The API lets you create shock waves in a single line of code.
The filter that renderes the waves will be created internally when needed.

``` java
postProcessing.triggerShockwave($(10, 20), ShockwaveOptions.radius(40));
```

I'm planning to add some more support for such locally 

<!-- truncate -->