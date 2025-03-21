### 🚀 Features & improvements

- Added color palette shaders `SUNBURN` and `GAMEBOY` using new `ColorPaletteShader` (#550)
- Added shader helpers that create dynamically configured sub `IntRangeShader` and `DoubleRangeShader` shaders (#573)
- Added `PixelateShader` #514
- Added difference measurement between two colors
- Retrieve color palette from frame
- Replaced light fullBrightnessArea with aerial light (#456)
- Draw shader using fixed progress value
- Tween shader progress (#534)
- Added `AerialLightComponent` for aerial illumination using the ecs

### 🪛 Bug Fixes

- Reflections and lightmaps are no longer affected by overlay shader
- Fixed wrong color alpha return value

### 🧽 Cleanup & refactoring

- Added links to online documentation to JavaDoc
- No minimum opacity when applying invert alpha on lightmap
- Introduced `Percent.rangeValue(from, to)` to cleanup existing code base (#572)

### 📦 Dependency updates

- Bump Mockito to 5.16.1