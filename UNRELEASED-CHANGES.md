### 🚀 Features & improvements

- Added ability to attach entities to other entities via `AttachmentComponent`
- Added ability to specify if shadow caster cast shadows over itself via `ShadowCasterComponent.selfShadow`
- Added new example application for top down games `VacuumOutlawApp`
- Added `CursorAttachmentComponent` / `System`
- Added `Line.middle()`
- Added `Entity.addIfNotPresent(Component)`
- Added `Bounds.contains(Bounds)`
- Added `Duration.progress(Time, Time)`
- Added `Environment.enableAllFeatures()` (#336)
- Added new asset `SoundBundle.SPLASH`

### 🪛 Bug Fixes

- Fixed crash when loading Tiled maps with layers containing `class` attribute
- Fixed light map shift on outer edges of the screen
- Fixed another threading issue preventing the engine to quit on errors

### 🧽 Cleanup & refactoring

- Made `SystemOrder` inner class of `Order` annotation 
- Updated package names of example applications
- Renamed `LightBlockingComponent` to `ShadowCasterComponent`
- Renamed scene switch extro to outro
- Enhanced performance of image blurring / applying light effects
- Removed `Light.addShadowCasters(List)`
- Tiled tiles use `class` as name instead of custom property `name` (#333)

### 📦 Dependency updates

- ...