### 🚀 Features & improvements

- Added `Environment.enableCoreFeatures()` to enable all featues but light.
- Added `Environment.entityCount(Archetype)` to count entities matching a specified archetype.

### 🪛 Bug Fixes

- Pinned Ubuntu version for github actions because of build error with latest version

### 🧽 Cleanup & refactoring

- Renamed `Environment.saveToFile`, `.deleteSavegameFile`, `.savegameFileExists`
- Added `Validate.notNull(Object, String)`

### 📦 Dependency updates

- Bump Mockito to 5.13.0