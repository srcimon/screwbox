---
title: Real-time smoke
authors: [ srcimon ]
tags: [ new-features ]
---

![occluders.png](../docs/core-modules/graphics/smoke.png)

Hello my friends,

For the past few weeks, I have been completely down the rabbit hole of fluid dynamics. These physics effects have always fascinated me, and I knew I had to bring them into ScrewBox.
The latest version now provides a first look for this new feature which is still heavily under development and cleanup.

The system is currently used to create realistic, dynamic smoke effects.
The fluid dynamic system I've come up with only simulates the content on and around the screen.
This is due to the restriction cpu because I wanted to provide a real-time simulation which cannot handle bigger grids at the moment.

Currently the system is able to emit multi colored smoke and put it into motion.
Also smoke can be rendered using differen styles to match the game graphics. 

More details are found int the [graphics documentation](/docs/core-modules/graphics#smoke).

Special thanks to [The coding train](https://www.youtube.com/watch?v=alhpH6ECFvQ) for the excellent video on the topic.

<!-- truncate -->