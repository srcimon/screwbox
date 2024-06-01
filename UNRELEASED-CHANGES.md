### 🚀 Features & improvements

- Added ability to attach entities to other entities via `AttachmentComponent`
- Added ability to specify if shadow caster cast shadows over itself via `ShadowCasterComponent.selfShadow`
- Added `Line.middle()`

### 🪛 Bug Fixes

- Fixed crash when loading Tiles maps with layers containing `class` attribute
- Fixed light map shift on outer edges of the screen

### 🧽 Cleanup & refactoring

- Made `SystemOrder` inner class of `Order` annotation 
- Updated package names of example applications
- Renamed `LightBlockingComponent` to `ShadowCasterComponent`
- Enhanced performance of image blurring / applying light effects
- Removed `Light.addShadowCasters(List)`
- Tiled tiles use `class` as name instead of custom property `name` (#333)

### 📦 Dependency updates

- ...