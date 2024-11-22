![Project Logo](docs/logo.png)

Minimalist 2D Java game engine. Result of covid lockdown.

[![Maven Central](https://img.shields.io/maven-central/v/io.github.srcimon/screwbox)](https://search.maven.org/artifact/io.github.srcimon/screwbox)
[![javadoc](https://javadoc.io/badge2/io.github.srcimon/screwbox-core/javadoc.svg)](https://javadoc.io/doc/io.github.srcimon/screwbox-core)
[![Build](https://github.com/srcimon/screwbox/actions/workflows/build.yml/badge.svg)](https://github.com/srcimon/screwbox/actions/workflows/build.yml)
[![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=srcimon_screwbox&metric=ncloc)](https://sonarcloud.io/summary/new_code?id=srcimon_screwbox)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=srcimon_screwbox&metric=coverage)](https://sonarcloud.io/summary/new_code?id=srcimon_screwbox)

## About

ScrewBox is a small pure Java 2D game engine.
I started developing ScrewBox in february 2021 right during too much time at hand because of covid lockdown.
I use it to learn about Java and have some fun.
If you want to get something startet in a few minutes ScrewBox might be a fun choice.

<p align="center"><a href="https://www.youtube.com/watch?v=fg5MJDx78SQ)"><img alt="youtube trailer" src="docs/trailer-thumbnail.png"></a>
<br/><b><a href="https://www.youtube.com/watch?v=fg5MJDx78SQ)">▶ Watch trailer on Youtube</a></b></p>

## Features

- **Entity Component System** Add and remove game objects and behaviour. Save and load the game state.
- **Render Engine** Fast rendering of shapes, animated graphics and text. Enhance graphics with dynamic light and shadow
  effects. Use split screen mode to create local multiplayer games.
- **Physics System** Move objects and resolve collisions. Detect obstacles via raycasting and use pathfinding to move around.
- **Particle Effects** Add particle effects to create some nice visuals.
- **Asset Management** Load game assets without interrupting the game flow.
- **Basic UI** Create an animated interactive game ui in an instant.
- **Input Support** Receive player interactions via keyboard and mouse.
- **Game Scenes** Use scenes to structure different game situations. Add animated transitions to smoothly switch between the scenes.
- **Audio Support** Play wav and midi sounds. Control the volume and pan manually or automatically based on the position of the sound source. Get information on whats currently playing.
- **Support for Tiled Editor** Import your game map and tilesets in Json format from
  the [Tiled Editor](https://www.mapeditor.org)

## Getting started

1. Create a new Maven project and add `screwbox-core` dependency (Java 21 required):

    ``` xml
    <dependency>
        <groupId>io.github.srcimon</groupId>
        <artifactId>screwbox-core</artifactId>
        <version>2.7.0</version>
    </dependency>
    ```

2. Create new class and run (JVM option `-Dsun.java2d.opengl=true` highly recommended)

    ``` java
    import io.github.srcimon.screwbox.core.Engine;
    import io.github.srcimon.screwbox.core.ScrewBox;
    import io.github.srcimon.screwbox.core.graphics.drawoptions.TextDrawOptions;
    
    import static io.github.srcimon.screwbox.core.assets.FontBundle.BOLDZILLA;
    
    public class HelloWorldApp {
    
        public static void main(String[] args) {
            Engine screwBox = ScrewBox.createEngine();
    
            screwBox.environment().addSystem(engine -> {
                var mousePosition = engine.mouse().position();
                var textDrawOptions = TextDrawOptions.font(BOLDZILLA).scale(3).alignCenter();
                var text = "ScrewBox is running at %s fps".formatted(engine.loop().fps());
                engine.graphics().world().drawText(mousePosition, text, textDrawOptions);
            });
    
            screwBox.start();
        }
    }
    ```

## Modules

Here is a quick overview over all modules contained in this project:

### screwbox

BOM to manage all project dependencies.

``` xml
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>io.github.srcimon</groupId>
            <artifactId>screwbox</artifactId>
            <version>2.7.0</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
```

### screwbox-core

Adds the core functionality of the ScrewBox engine. Nothing more needed to make game.

``` xml
<dependency>
    <groupId>io.github.srcimon</groupId>
    <artifactId>screwbox-core</artifactId>
</dependency>
```

``` java
// creating a fancy black window
ScrewBox.createEngine().start();
```

### screwbox-tiled

Adds support for tilesets and maps made with [Tiled Editor](https://www.mapeditor.org). For real code have a look at
the [pathfinding example](./examples/pathfinding).

``` xml
<dependency>
    <groupId>io.github.srcimon</groupId>
    <artifactId>screwbox-tiled</artifactId>
</dependency>
```

``` java
// loading a map made with Tiled Editor
Map map = Map.fromJson("underworld_map.json");
```

## Documentation

ScrewBox packs some examples. You can inspect these examples to learn how to use the engine.

| Example                                       | Description                                                                                                          |
|-----------------------------------------------|----------------------------------------------------------------------------------------------------------------------| 
| **[hello-world](./examples/hello-world)**     | Hello world application with some interactive particle effects.                                                      |
| **[pathfinding](./examples/pathfinding)**     | Example application showing how to use pathfinding and importing maps from [Tiled Editor](https://www.mapeditor.org) |
| **[game-of-life](./examples/game-of-life)**   | An interactive game of life implementation.                                                                          |
| **[platformer](./examples/platformer)**       | A much more complex example showing how to make a platformer.                                                        |
| **[vacuum-outlaw](./examples/vacuum-outlaw)** | Example for a top down game.                                                                                         |

## Libraries used

- JUnit [Eclipse Public License 2.0](https://github.com/junit-team/junit5/blob/main/LICENSE.md)
- FasterXML Jackson Core [Apache License 2.0](https://github.com/FasterXML/jackson-core/blob/2.14/LICENSE)
- AssertJ [Apache License 2.0](https://github.com/assertj/assertj-core/blob/main/LICENSE.txt)
- Mockito [MIT License](https://github.com/mockito/mockito/blob/main/LICENSE)

## Acknowledgments

The project idea was inspired by Gurkenlabs [Litiengine](https://github.com/gurkenlabs/litiengine).

<p align="center"><img alt="super hero and cat standing next to each other" src="docs/outro.gif"></p>
