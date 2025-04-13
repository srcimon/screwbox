---
sidebar_position: 1
---

# Common data types

This is an overview over the common data types used in ScrewBox.
To get more detailed information please have a look the [JavaDoc](https://javadoc.io/doc/dev.screwbox.core/screwbox-core/latest/index.html).

## General

| Type      | Description                                                                   |
|-----------|-------------------------------------------------------------------------------|
| `Percent` | A percent value between 0.0 and 1.0.                                          |
| `Ease`    | Configures the direction and the progress of a value change. [See Ease](ease) |

## Space

| Type       | Description                                                                                                   |
|------------|---------------------------------------------------------------------------------------------------------------|
| `Vector`   | A position or a distance in the 2d world.                                                                     |
| `Bounds`   | A square area in the 2d world. The upper left corner is called **origin**, the center is called **position**. |
| `Offset`   | A pixel perfect position on the screen.                                                                       |
| `Size`     | A pixel perfect size of an area.                                                                              |
| `Line`     | The line between two distinct **vectors**.                                                                    |
| `Path`     | A path with multiple nodes.                                                                                   |
| `Rotation` | Rotation in degrees.                                                                                          |

## Time

| Type       | Description                                                                                                                                                |
|------------|------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `Time`     | A point in time with nano accuracy. Can only be used to measure **Duration** relative to other **Time** instance Loses meaning when shutting down the JVM. |
| `Duration` | The duration between one point in time and another.                                                                                                        |

