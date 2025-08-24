---
title: Lessons on performance
authors: [srcimon]
tags: [lessons-learned]
---
I must have spends hundreds of hours on enhancing the performance of ScrewBox,
but today I learned that I have missed some pretty obvious basics.

For context: I am working on a MacOs machine and when I started graphic programming with Java
I learned that there was a pretty unavoidable system property that must be set to avoid software
rendering and therefore low fps values: `sun.java2d.opengl=true`.

When I discovered the option this massively enhanced the speed of apps created on ScrewBox.

When JDK 21 added support for the Metal API on MacOs i was quite disappointed that this didn't enhance
the performance at all.
But man was I wrong.

ScrewBox warns you, when you are not using the opengl setting and my local run configurations already packed these
settings so I didn't question this after so many hours getting used to enforce this setting.

Today I discovered that removing the setting allows Mac to default to the Metal API and therefore push the performance
from 800 to 1000 fps in one app and from 8000 to 11000 fps in another with much less content.

Version 3.8.0 will remove the necessary warning on MacOs and will automatically default to OpenGl on windows machines.
There is no longer a need to specify JVM start parameters when working on Windows.
Docs are already updated.