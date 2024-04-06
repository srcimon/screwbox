### 🚀 Features & improvements

- Optimized drawing of sprite fill using `SpriteFillOptions` (#242)
- Slightly improved rendering performance due to performance optimized rendering hints
- Major performance improvement on drawing image fill
- Added `AssetBundle` to mark mark enums as `AssetLocation`
- Moved all pre defined assets to new enums `SpriteBundle`,`SoundBundle` and `FontsBundle` (#191)
- Added new narrow font `SKINNY_SANS` (#245)
- ScrewBox apps close much faster now

### 🪛 Bug Fixes

- Fixed `BlurImageFilter` unwanted scaling
- Fixed `Window.open()` causes flickering when window is already open
- Fixed window not closing when asyc task crashes right when window is opening
- Added Thread.sleep before fullscreen changes on MacOs (previously removed in 1.5.0) to fix issue changing resolution in fullscreen

### 🧽 Cleanup & refactoring

- Added utility method `Window.isClosed()`

### 📦 Dependency updates

- ...