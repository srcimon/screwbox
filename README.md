![Project Logo](docs/logo.png)

Minimalist 2D Java game engine. Result of covid lockdown.

[![Maven Central](https://img.shields.io/maven-central/v/io.github.srcimon/screwbox)](https://search.maven.org/artifact/io.github.srcimon/screwbox)
[![javadoc](https://javadoc.io/badge2/io.github.srcimon/screwbox-core/javadoc.svg)](https://javadoc.io/doc/io.github.srcimon/screwbox-core)
[![Build](https://github.com/srcimon/screwbox/actions/workflows/build.yml/badge.svg)](https://github.com/srcimon/screwbox/actions/workflows/build.yml)
[![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=srcimon_screwbox&metric=ncloc)](https://sonarcloud.io/summary/new_code?id=srcimon_screwbox)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=srcimon_screwbox&metric=coverage)](https://sonarcloud.io/summary/new_code?id=srcimon_screwbox)

## About

ScrewBox is a minimalist pure Java game engine.
If you want to start building your own 2D game without leaving your cozy IDE it might be a fun choice.

<p align="center"><a href="https://www.youtube.com/watch?v=fg5MJDx78SQ)"><img alt="youtube trailer" src="docs/screenshot.png"></a>

## Features

- **Entity Component System** Add and remove game objects and behaviour. Save and load the game state.
- **Render Engine** Fast rendering of shapes, animated graphics and text. Enhance graphics with dynamic light and shadow
  effects. Use split screen mode to create local multiplayer games.
- **Physics System** Move objects and resolve collisions. Detect obstacles via raycasting and use pathfinding to move
  around.
- **Asset Management** Load game assets without interrupting the game flow.
- **Particle Effects** Add particle effects to create some nice visuals.
- **Basic UI** Create an animated interactive game ui in an instant.
- **Input Support** Receive player interactions via keyboard and mouse.
- **Scene Management** Use scenes to structure different game situations. Add animated transitions to smoothly switch
  between the scenes.
- **Audio Support** Play wav and midi sounds. Control the volume and pan manually or automatically based on the position
  of the sound source. Get information on whats currently playing.
- **Archivements** Add archivements to challange players with custom goals.
- **Support for Tiled Editor** Import your game map and tilesets in Json format from
  the [Tiled Editor](https://www.mapeditor.org)

## Getting started

1. Create a new Maven project using Java 21+ and add the `screwbox-core` dependency:

    ``` xml
    <dependency>
        <groupId>io.github.srcimon</groupId>
        <artifactId>screwbox-core</artifactId>
        <version>2.12.0</version>
    </dependency>
    ```

2. Create a minimal application using example code:

    ``` java
    import io.github.srcimon.screwbox.core.Engine;
    import io.github.srcimon.screwbox.core.ScrewBox;
    import io.github.srcimon.screwbox.core.graphics.drawoptions.TextDrawOptions;

    import static io.github.srcimon.screwbox.core.assets.FontBundle.BOLDZILLA;

    public class HelloWorldApp {

    public static void main(String[] args) {
        Engine screwBox = ScrewBox.createEngine();

        screwBox.environment().addSystem(engine -> engine.graphics().canvas().drawText(
                engine.mouse().offset(),
                "current fps is: " + engine.loop().fps(),
                TextDrawOptions.font(BOLDZILLA).scale(3).alignCenter()));

        screwBox.start();
    }
    ```

3. Run main method (it's recommended to use JVM option `-Dsun.java2d.opengl=true` for much better performance)

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
            <version>2.12.0</version>
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

ScrewBox packs some examples. You can inspect these examples to learn how to use the engine. If you have any questions
please let me know by creating an [Issue](https://github.com/srcimon/screwbox/issues).

| Example                                       | Description                                                                                                          |
|-----------------------------------------------|----------------------------------------------------------------------------------------------------------------------|
| **[hello-world](./examples/hello-world)**     | Hello world application with some interactive particle effects.                                                      |
| **[pathfinding](./examples/pathfinding)**     | Example application showing how to use pathfinding and importing maps from [Tiled Editor](https://www.mapeditor.org) |
| **[game-of-life](./examples/game-of-life)**   | An interactive game of life implementation.                                                                          |
| **[platformer](./examples/platformer)**       | A much more complex example showing how to make a platformer.                                                        |
| **[vacuum-outlaw](./examples/vacuum-outlaw)** | Example for a top down game.                                                                                         |
| **[playground](./examples/playground)**       | Just a playground, containing whatever is currently in focus of development.                                         |

## Libraries used

- JUnit [Eclipse Public License 2.0](https://github.com/junit-team/junit5/blob/main/LICENSE.md)
- FasterXML Jackson Core [Apache License 2.0](https://github.com/FasterXML/jackson-core/blob/2.14/LICENSE)
- AssertJ [Apache License 2.0](https://github.com/assertj/assertj-core/blob/main/LICENSE.txt)
- Mockito [MIT License](https://github.com/mockito/mockito/blob/main/LICENSE)

## Acknowledgments

The project idea was inspired by Gurkenlabs [Litiengine](https://github.com/gurkenlabs/litiengine).

<p align="center"><img alt="super hero and cat standing next to each other" src="docs/outro.gif"></p>
