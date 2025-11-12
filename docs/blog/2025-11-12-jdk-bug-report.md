---
title: JDK bug report
authors: [ srcimon ]
tags: [ lessons-learned ]
---

Hey everyone,

A few days ago, I watched a YouTube video on pixel snapping effects in 2D games, which are even present in highly popular games.
I was curious if ScrewBox also suffers from these issues.
Upon closer inspection, I noticed some strange effects on the edges of the game window.
The effects were rarely visible and I didn't notice them before because they only appeare next to the screen edges.

After investigating for a few hours, I identified the error as the metal render on MacOs.
However, I couldn’t find any workaround other than switching to OpenGL rendering, which is significantly slower on Mac because it’s outdated.

I shared this Java bug with a colleague, and she expressed indifference if it wasn’t officially reported.
Of course, she was right, so I invested a few more hours to report the bug, which was reviewed and now is officially accepted as [JDK-8371679](https://bugs.java.com/bugdatabase/view_bug?bug_id=JDK-8371679).

So you could say I found a genuine **edge case bug**  in the JDK.😏

<!-- truncate -->