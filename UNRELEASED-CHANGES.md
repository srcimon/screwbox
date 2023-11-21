## Changes

### 🚀 Features & improvements

- added new way to load a sound `Sound.fromWav(content)`
- added missing keys `CONTROL`, `PAGE_DOWN`, `PAGE_UP`, `A`, to `Z`,  `F1` to `F12`
- added `Color.ORANGE`
- `Screen`, `World` and `Audio` accept `Supplier<T>` instead of `Asset<T>`
- renamed `Graphics` methods for offset / postion conversion to `toOffset`, `toPosition`

### 🪛 Fixed issues

- fixed exception when calling `Reflections.findClassesInPackage(package)` on Windows

### 🧽 Cleanup & refactoring

- moved osx-detection to new class `MacOsSupport`
- added tests and various small refactorings

### 📦 Dependency updates

- bump junit-jupiter from 5.10.0 to 5.10.1