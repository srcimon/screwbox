![Project Logo](logo.png) 

Minimalist 2d Java game engine. Result of covid lockdown.

[![Build](https://github.com/simonbas/screwbox/actions/workflows/build.yml/badge.svg)](https://github.com/simonbas/screwbox/actions/workflows/build.yml) [![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=simonbas_screwbox&metric=ncloc)](https://sonarcloud.io/summary/new_code?id=simonbas_screwbox) [![Coverage](https://sonarcloud.io/api/project_badges/measure?project=simonbas_screwbox&metric=coverage)](https://sonarcloud.io/summary/new_code?id=simonbas_screwbox)

## Current status
ScrewBox is a personal pet project since february 2021. It has no constant commits and has not been released yet.

## Features
- **Entity Component Engine** Add and remove game objects and behaviour. Save and load the game state.
- **Render Engine** Fast rendering of shapes, animated graphics and text. Enhance graphics with dynamic light and shadow effects.
- **Physics System** Move objects and resolve collisions. Detect objects via raycasting.
- **Asset Management** Load game assets without interrupting the game.
- **Basic UI** Create an animated interactive game ui in an instant.
- **Input Support** Receive player interactions via keyboard and mouse.
- **Game Scenes** Use scenes to structure different game situations.
- **Audio Support** Play wav and midi sounds. Control the volume. Thats it. Only the basics.
- **Support for Tiled Editior** Import your game map and tilesets in Json format from the [Tiled Editor](https://www.mapeditor.org)

## Getting started
1. Clone this repository.
2. Run `maven install`
3. Create your own Maven project.
4. Add dependency:

``` xml
<dependency>
  <groupId>de.suzufa.screwbox</groupId>
  <artifactId>screwbox-core</artifactId>
  <version>0.0.1-SNAPSHOT</version>
</dependency>
```
5. Create your first game and run.

``` java
package de.suzufa.screwbox.examples.helloworld;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.ScrewBox;
import de.suzufa.screwbox.core.entities.EntitySystem;
import de.suzufa.screwbox.core.graphics.Color;
import de.suzufa.screwbox.core.graphics.Offset;
import de.suzufa.screwbox.core.graphics.Pixelfont;

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
    - **PathfindingExample** example showing how to use pathfinding and importing maps from [Tiled Editor](https://www.mapeditor.org)
    - **PlatformerExample** a more complex example showing how to make a platformer

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
- [ ] add menu bar when this nasty jdk bug is resolved: https://bugs.openjdk.org/browse/JDK-8270888
- [ ] add headless mode with scripted events for automating the engine