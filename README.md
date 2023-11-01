![Project Logo](docs/logo.png)

Minimalist 2d Java game engine. Result of covid lockdown.

[![Build](https://github.com/srcimon/screwbox/actions/workflows/build.yml/badge.svg)](https://github.com/srcimon/screwbox/actions/workflows/build.yml) [![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=srcimon_screwbox&metric=ncloc)](https://sonarcloud.io/summary/new_code?id=srcimon_screwbox) [![Coverage](https://sonarcloud.io/api/project_badges/measure?project=srcimon_screwbox&metric=coverage)](https://sonarcloud.io/summary/new_code?id=srcimon_screwbox)

## About

ScrewBox is a personal pet project since february 2021. I use it to learn about Java and designing larger libaries.
The result is designed for my enjoyment, not necessarily for yours.
So please be understanding of any inconsistencies in the API or if you find any errors.

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
      <groupId>io.github.srcimon</groupId>
      <artifactId>screwbox-core</artifactId>
      <version>0.0.1-SNAPSHOT</version>
    </dependency>
    ```

5. Create your first game and run. Recommended JVM Options `-Dsun.java2d.opengl=true` and only on
   osx `--add-opens java.desktop/com.apple.eawt=ALL-UNNAMED`.

    ``` java
    package io.github.srcimon.screwbox.examples.helloworld;
    
    import io.github.srcimon.screwbox.core.Engine;
    import io.github.srcimon.screwbox.core.ScrewBox;
    import io.github.srcimon.screwbox.core.graphics.Color;
    import io.github.srcimon.screwbox.core.graphics.Offset;
    import io.github.srcimon.screwbox.core.graphics.Pixelfont;
    
    public class HelloWorldExample {
    
        public static void main(String[] args) {
            Engine screwBox = ScrewBox.createEngine("Hello World Example");
    
            screwBox.entities().add(engine -> {
                Offset position = engine.mouse().position();
                Pixelfont font = Pixelfont.defaultFont(Color.WHITE);
                engine.graphics().screen().drawTextCentered(position, "HELLO WORLD!", font, 4);
            });
    
            screwBox.start();
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

- [ ] asset preloading on Windows crashes the program (invalid pattern)

## Libraries used

- JUnit: [Eclipse Public License 2.0](https://github.com/junit-team/junit5/blob/main/LICENSE.md)
- FasterXML Jackson Core [Apache License 2.0](https://github.com/FasterXML/jackson-core/blob/2.14/LICENSE)
- AssertJ [Apache License 2.0](https://github.com/assertj/assertj-core/blob/main/LICENSE.txt)
- Mockito [MIT License](https://github.com/mockito/mockito/blob/main/LICENSE)

## Future ideas and plans

- [ ] add game example like [in this video](https://www.youtube.com/watch?v=GDoBw1ogFZY)
- [ ] add effects that can be applied to `Frame`s to create an animation (wind on foliage, water)
- [ ] add `LightDebugSystem`
- [ ] add `window.openDebugMenu()` allows change of scenes
- [ ] add small `Pixelfont` (crop transparent image area)
- [ ] publish via Maven-Central (
  see [Tutorial](https://theoverengineered.blog/posts/publishing-my-first-artifact-to-maven-central-using-github-actions))
- [ ] Support for animated light
- [ ] particle emitters
- [ ] add menu bar
- [ ] fix bug: no more stderror output (maybe osx bug in
  13.1: [Stackoverflow](https://stackoverflow.com/questions/74609260/r-warnings-errors-in-a-fresh-install), [Jetbrains](https://youtrack.jetbrains.com/issue/PY-58036))

## Acknowledgments

The project idea was inspired by Gurkenlabs [Litiengine](https://github.com/gurkenlabs/litiengine).


<p align="center"><img alt="super hero and cat standing next to each other" src="docs/outro.gif"></p>
