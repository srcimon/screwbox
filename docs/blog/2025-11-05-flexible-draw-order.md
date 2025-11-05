---
title: Flexible draw order
authors: [srcimon]
tags: [milestones]
---

Hey everyone,

in the past, ScrewBox had some very restrictive limitations due to the architecture decisions I made, both consciously and unconsciously.
For instance, I never considered implementing a split-screen mode within the existing code base. However, it was possible, but it was a significant challenge to rewrite hundreds of lines of code.
After at least three months of work, finally getting it done felt like a huge success.

The upcoming version [3.14.0](https://github.com/srcimon/screwbox/releases/tag/3.14.0) will finally address another restriction that had been bothering me.
Unfortunately, it doesn’t sound particularly exciting:
Now, it’s possible to draw entities in any order across all entity systems.
This allows for some nice refactorings within the engine, which results in slight performance improvements.
However, it also provides much more freedom when creating games with ScrewBox.

<details>
<summary>To draw across entity system execution order simply add the order offset to your drawing call.</summary>
``` java
// will be drawn second
canvas.drawLine(line, LineDrawOptions.color(RED).drawOrder(2));

// will be drawn first
canvas.drawLine(line, LineDrawOptions.color(RED).drawOrder(1));

// will be drawn above the light
int aboveLight = Order.PRESENTATION_LIGHT.mixinDrawOrder(1);
canvas.drawLine(line, LineDrawOptions.color(RED).drawOrder(aboveLight));
```
</details>

<!-- truncate -->