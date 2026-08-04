---
title: Vape-Coding
authors: [ srcimon ]
tags: [ new-features, milestones ]
---

![occluders.png](../docs/core-modules/graphics/smoke.png)

Hello my friends,

for the past few weeks, I have been completely down the rabbit hole of fluid dynamics.
These physics effects have recently fascinated me, and I knew I had to bring them into ScrewBox.
I've read [the number one paper](https://graphics.cs.cmu.edu/nsp/course/15-464/Fall09/papers/StamFluidforGames.pdf) on the topic,
watched a lot of YouTube tutorials and finally got stuck with the one from [The coding train](https://www.youtube.com/watch?v=alhpH6ECFvQ).
I cannot say that I really understand the underlaying math, I've only got it as long as I needed it to get it right.
It really fascinates me what bugs I've created when implementing it and there are still some rough edge cases to deal with.

The system is currently used to create realistic, interactive smoke effects.
The fluid dynamic system I've come up with only simulates the content on and around the screen and not the whole game world.
This is due to the restriction cpu power because I wanted to provide a real-time simulation which cannot handle bigger grids at the moment.
The latest version now provides a first look for this new feature which is still heavily under development and cleanup.

Currently the system is able to emit multi colored smoke and put it into motion.
Also smoke can get deflected by obstacles.
You can use different rendering styles to match the game graphics. 
There are still lots of optimizations and ideas I want to implement in the comming weeks.
E.g. I could think of using the fluid system to enhance fluid graphics or create special shaders as well.

If you want to try it out please let me know any issue that you might have.
More details are found in the [graphics documentation](/docs/core-modules/graphics#smoke).

<!-- truncate -->