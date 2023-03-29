![Project Logo](docs/logo.png)

Minimalist 2d Java game engine. Result of covid lockdown.

[![Build](https://github.com/simonbas/screwbox/actions/workflows/build.yml/badge.svg)](https://github.com/simonbas/screwbox/actions/workflows/build.yml) [![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=simonbas_screwbox&metric=ncloc)](https://sonarcloud.io/summary/new_code?id=simonbas_screwbox) [![Coverage](https://sonarcloud.io/api/project_badges/measure?project=simonbas_screwbox&metric=coverage)](https://sonarcloud.io/summary/new_code?id=simonbas_screwbox)


## Current status
ScrewBox is a personal pet project since february 2021. It has no constant commits and has not been released yet.

## Features

- **Entity Component Engine** Add and remove game objects and behaviour. Save and load the game state.
- **Render Engine** Fast rendering of shapes, animated graphics and text. Enhance graphics with dynamic light and shadow
  effects.
- **Physics System** Move objects and resolve collisions. Detect objects via raycasting.
- **Asset Management** Load game assets without interrupting the game.
- **Basic UI** Create an animated interactive game ui in an instant.
- **Input Support** Receive player interactions via keyboard and mouse.
- **Game Scenes** Use scenes to structure different game situations.
- **Audio Support** Play wav and midi sounds. Control the volume. Thats it. Only the basics.
- **Support for Tiled Editior** Import your game map and tilesets in Json format from
  the [Tiled Editor](https://www.mapeditor.org)

## Getting started

1. Clone this repository.
2. Run `maven install`
3. Create your own Maven project.
4. Add dependency:

``` xml
<dependency>
  <groupId>io.github.simonbas</groupId>
  <artifactId>screwbox-core</artifactId>
  <version>0.0.1-SNAPSHOT</version>
</dependency>
```
5. Create your first game and run. Recommended JVM Options `-Dsun.java2d.opengl=true` and only on
   osx `--add-opens java.desktop/com.apple.eawt=ALL-UNNAMED`.

``` java
package io.github.simonbas.screwbox.examples.helloworld;

import io.github.simonbas.screwbox.core.Engine;
import io.github.simonbas.screwbox.core.ScrewBox;
import io.github.simonbas.screwbox.core.entities.EntitySystem;
import io.github.simonbas.screwbox.core.graphics.Color;
import io.github.simonbas.screwbox.core.graphics.Offset;
import io.github.simonbas.screwbox.core.graphics.Pixelfont;

public class HelloWorldExample {

    public static void main(String[] args) {
        Engine engine = ScrewBox.createEngine("Hello World Example");

        engine.entities().add(new EntitySystem() {

            @Override
            public void update(Engine engine) {
                Offset position = engine.mouse().position();
                Pixelfont font = Pixelfont.defaultFont(Color.WHITE);
                engine.graphics().screen().drawTextCentered(position, "HELLO WORLD!", font, 4);
            }
        });

        engine.start();
    }
}
```

6. Check out the example applications:
    - **HelloWorldExample** an hello world application
    - **GameOfLifeExample** an interactive game of life implementation
    - **PathfindingExample** example showing how to use pathfinding and importing maps
      from [Tiled Editor](https://www.mapeditor.org)
    - **PlatformerExample** a more complex example showing how to make a platformer

## Known issues

- [ ] massive performance drop on JDK > 18 when not using `-Dsun.java2d.opengl=true`
- [ ] asset preloading on Windows crashes the program (invalid pattern)

## Libraries used

- JUnit: [Eclipse Public License 2.0](https://github.com/junit-team/junit5/blob/main/LICENSE.md)
- FasterXML Jackson Core [Apache License 2.0](https://github.com/FasterXML/jackson-core/blob/2.14/LICENSE)
- AssertJ [Apache License 2.0](https://github.com/assertj/assertj-core/blob/main/LICENSE.txt)
- Mockito [MIT License](https://github.com/mockito/mockito/blob/main/LICENSE)

## Acknowledgments

The project idea was inspired by Gurkenlabs [Litiengine](https://github.com/gurkenlabs/litiengine).

## Ideas and plans

- [x] add A*-Pathfinding Algorithm (because speed)
- [ ] add Support for animated light
- [ ] add particle emitters
- [ ] add menu bar with native fullscreen on osx (Bug: https://bugs.openjdk.org/browse/JDK-8270888)
- [ ] add headless mode with scripted events for automating the engine
- [ ] bug: no more stderror output (maybe osx bug in
  13.1: [Stackoverflow](https://stackoverflow.com/questions/74609260/r-warnings-errors-in-a-fresh-install), [Jetbrains](https://youtrack.jetbrains.com/issue/PY-58036))

<p align="center"><img src="docs/outro.gif"></p>
