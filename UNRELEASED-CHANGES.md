## v1.0.0-RC2

### 🚀 Features & improvements

- Added new way to load a sound `Sound.fromWav(content)`
- Added `Pixelfont.defaultFont()` with white as default color
- `Asset.load` returns boolean indicating whether loading happend or wasn't necessary
- Added missing keys `CONTROL`, `PAGE_DOWN`, `PAGE_UP`, `A`, to `Z`,  `F1` to `F12`
- Added `Color.ORANGE`

### 🪛 Bug Fixes

- Fixed exception when calling `Reflections.findClassesInPackage(package)` on Windows

### 🧽 Cleanup & refactoring

- Moved osx-detection to new class `MacOsSupport`
- `Screen`, `World` and `Audio` accept `Supplier<T>` instead of `Asset<T>`
- Renamed `Graphics` methods for offset / postion conversion to `toOffset`, `toPosition`
- Renamed `Mouse` methods for current position and offset to `position`, `offset`
- Renamed `WindowBounds` to `ScreenBounds`
- Renamed `Timer` to `Scheduler`
- Added additional tests and various small refactorings
- Added JavaDoc to `Archetype`

### 📦 Dependency updates

- Bump junit-jupiter to 5.10.1