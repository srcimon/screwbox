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

| Preview                                           | Shader                              |
|---------------------------------------------------|-------------------------------------|
| ![NONE](NONE.gif)                                 | -                                   |
| ![BREEZE](BREEZE.gif)                             | `ShaderBundle.BREEZE`               |
| ![DISSOLVE](DISSOLVE.gif)                         | `ShaderBundle.DISSOLVE`             |
| ![SUNBURN](SUNBURN.gif)                           | `ShaderBundle.SUNBURN`              |
| ![GAMEBOY](GAMEBOY.gif)                           | `ShaderBundle.GAMEBOY`              |
| ![PIXELATE](PIXELATE.gif)                         | `ShaderBundle.PIXELATE`             |
| ![FOLIAGE](FOLIAGE.gif)                           | `ShaderBundle.FOLIAGE`              |
| ![GREYSCALE](GREYSCALE.gif)                       | `ShaderBundle.GREYSCALE`            |
| ![INVERT_COLORS](INVERT_COLORS.gif)               | `ShaderBundle.INVERT_COLORS`        |
| ![IRIS_SHOT](IRIS_SHOT.gif)                       | `ShaderBundle.IRIS_SHOT`            |
| ![ALARMED](ALARMED.gif)                           | `ShaderBundle.ALARMED`              |
| ![SILHOUETTE](SILHOUETTE.gif)                     | `ShaderBundle.SILHOUETTE`           |
| ![HURT](HURT.gif)                                 | `ShaderBundle.HURT`                 |
| ![WATER](WATER.gif)                               | `ShaderBundle.WATER`                |
| ![SEAWATER](SEAWATER.gif)                         | `ShaderBundle.SEAWATER`             |
| ![SELECTED](SELECTED.gif)                         | `ShaderBundle.SELECTED`             |
| ![CHROMATIC_ABERRATION](CHROMATIC_ABERRATION.gif) | `ShaderBundle.CHROMATIC_ABERRATION` |
| ![OUTLINE](OUTLINE.gif)                           | `ShaderBundle.OUTLINE`              |
