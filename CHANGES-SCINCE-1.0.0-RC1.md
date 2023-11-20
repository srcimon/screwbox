## Changes

### 🚀 Features / Improvements

- added new way to load a sound `Sound.fromWav(content)`
- added keys to enum `Key`: `CONTROL`, `A`, `B`, `C`, `D`, `E`, `F`, `G`, `H`, `I`

### 🪛 Fixes

-  fixed crash when `Reflections.findClassesInPackage(package)` is called on Windows machines (e.g. Asset preloading)

### 🧽 Cleanup & refactoring

- moved osx-detection to new class `MacOsSupport`

### 📦 Dependency updates

- bump junit-jupiter from 5.10.0 to 5.10.1