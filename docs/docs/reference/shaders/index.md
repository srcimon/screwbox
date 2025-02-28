# Shaders

Shaders can be used to draw animated or still images with an applied effect.

## Usage example

``` java
canvas.drawSprite(SpriteBundle.BOX_STRIPED, engine.mouse().offset(), SpriteDrawOptions
    .originalSize()
    .shaderSetup(ShaderBundle.WATER));
```

## Overview

The asset bundle class `ShaderBundle` contains some pre defined shaders for easy use. Most of those shaders can be
individualized even more using the `ShaderSetup` class.


| Shader                        | Visualization                         |
|-------------------------------|---------------------------------------|
| `ShaderBundle.BREEZE`         | ![BREEZE](BREEZE.gif)                 |
| `ShaderBundle.GRAYSCALE`      | ![GRAYSCALE](GRAYSCALE.gif)           |
| `ShaderBundle.INVERT_COLORS`  | ![INVERT_COLORS](INVERT_COLORS.gif)   |
| `ShaderBundle.FLASHING_RED`   | ![FLASHING_RED](FLASHING_RED.gif)     |
| `ShaderBundle.FLASHING_WHITE` | ![FLASHING_WHITE](FLASHING_WHITE.gif) |
| `ShaderBundle.WATER`          | ![WATER](WATER.gif)                   |
| `ShaderBundle.OUTLINE_BLACK`  | ![OUTLINE_BLACK](OUTLINE_BLACK.gif)   |
