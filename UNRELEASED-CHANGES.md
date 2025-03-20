### 🚀 Features & improvements

- Added two more shaders `SUNBURN` and `GAMEBOY` using new `ColorPaletteShader` (#550)
- Added difference measurement between two colors
- Retrieve color palette from frame
- Replaced light fullBrightnessArea with aerial light (#456)
- Draw shader using fixed progress value
- Tween shader progress (#534)
- Added `AerialLightComponent` for aerial illumination using the ecs

### 🪛 Bug Fixes

- Reflections and lightmaps are no longer affected by overlay shader

### 🧽 Cleanup & refactoring

- Added links to online documentation to JavaDoc
- No minimum opacity when applying invert alpha on lightmap

### 📦 Dependency updates

- Bump Mockito to 5.16.1