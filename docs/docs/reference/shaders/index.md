# Shaders

Shaders can be used to draw animated or still images with an applied effect.

## Usage example

``` java
canvas.drawSprite(SpriteBundle.BOX_STRIPED, engine.mouse().offset(), SpriteDrawOptions
    .originalSize()
    .shaderSetup(ShaderBundle.WATER));
```

## Overview

| Shader                        | Visualization                         |
|-------------------------------|---------------------------------------|
| `ShaderBundle.BREEZE`         | ![BREEZE](BREEZE.gif)                 |
| `ShaderBundle.FLASHING_RED`   | ![FLASHING_RED](FLASHING_RED.gif)     |
| `ShaderBundle.FLASHING_WHITE` | ![FLASHING_WHITE](FLASHING_WHITE.gif) |
| `ShaderBundle.GRAYSCALE`      | ![GRAYSCALE](GRAYSCALE.gif)           |
| `ShaderBundle.WATER`          | ![WATER](WATER.gif)                   |