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



| Shader                             | Visualization                                   |
|------------------------------------|-------------------------------------------------|
| `ShaderBundle.BREEZE`              | ![BREEZE](BREEZE.gif)                           |
| `ShaderBundle.GRAYSCALE`           | ![GRAYSCALE](GRAYSCALE.gif)                     |
| `ShaderBundle.INVERT_COLORS`       | ![INVERT_COLORS](INVERT_COLORS.gif)             |
| `ShaderBundle.FLASHING_RED`        | ![FLASHING_RED](FLASHING_RED.gif)               |
| `ShaderBundle.FLASHING_WHITE`      | ![FLASHING_WHITE](FLASHING_WHITE.gif)           |
| `ShaderBundle.WATER`               | ![WATER](WATER.gif)                             |
| `ShaderBundle.WATER_COMICAL`       | ![WATER_COMICAL](WATER_COMICAL.gif)             |
| `ShaderBundle.OUTLINE_PULSE_WHITE` | ![OUTLINE_PULSE_WHITE](OUTLINE_PULSE_WHITE.gif) |
| `ShaderBundle.OUTLINE_BLACK`       | ![OUTLINE_BLACK](OUTLINE_BLACK.gif)             |
