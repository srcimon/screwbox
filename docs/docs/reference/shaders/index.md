# Shaders

Shaders can be used to draw animated or still images with an applied effect.

## Usage example

The asset bundle class `ShaderBundle` contains some pre defined shaders for easy use. Most of those shaders can be
individualized even more using the `ShaderSetup` class.

``` java
// using bundled shader setup
canvas.drawSprite(SpriteBundle.BOX_STRIPED, engine.mouse().offset(), SpriteDrawOptions
    .originalSize()
    .shaderSetup(ShaderBundle.WATER));

// custom shader setup
    .shaderSetup(ShaderSetup.shader(new OutlineShader(Color.WHITE))));
```

## Overview

| Shader                              | Preview                                           |
|-------------------------------------|---------------------------------------------------|
| -                                   | ![NONE](NONE.gif)                                 |
| `ShaderBundle.BREEZE`               | ![BREEZE](BREEZE.gif)                             |
| `ShaderBundle.GRAYSCALE`            | ![GRAYSCALE](GRAYSCALE.gif)                       |
| `ShaderBundle.INVERT_COLORS`        | ![INVERT_COLORS](INVERT_COLORS.gif)               |
| `ShaderBundle.IRIS_SHOT`            | ![IRIS_SHOT](IRIS_SHOT.gif)                       |
| `ShaderBundle.ALARMED`              | ![ALARMED](ALARMED.gif)                           |
| `ShaderBundle.HURT`                 | ![HURT](HURT.gif)                                 |
| `ShaderBundle.WATER`                | ![WATER](WATER.gif)                               |
| `ShaderBundle.SEAWATER`             | ![SEAWATER](SEAWATER.gif)                         |
| `ShaderBundle.SELECTED`             | ![SELECTED](SELECTED.gif)                         |
| `ShaderBundle.CHROMATIC_ABERRATION` | ![CHROMATIC_ABERRATION](CHROMATIC_ABERRATION.gif) |
| `ShaderBundle.OUTLINE`              | ![OUTLINE](OUTLINE.gif)                           |
