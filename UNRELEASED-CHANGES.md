### 🚀 Features & improvements

- Added `Environment.entityCount(Archetype)` to count entities matching a specified archetype.
- Added `MovementTargetComponent` / `MovementTargetSystem` to move an entity towards a specific position (#376)
- Added `Ease.PLATEAU_OUT`, `PLATEAU_OUT_SLOW`

### 🪛 Bug Fixes

- Pinned Ubuntu version for github actions because of build error with latest version
- Fixed missing duration when replacing color of fram
- Fixed playback not stopped when `SoundComponent` is removed

### 🧽 Cleanup & refactoring

- Renamed `Environment.saveToFile`, `.deleteSavegameFile`, `.savegameFileExists`
- Renamed `MovementPathComponent`, `MovementPathSystem`, `MovementPathDebugSystem`
- Reworked `MovementPathSystem` now uses `MovementTargetComponent` to move the entity along the path. (#376)

### 📦 Dependency updates

- Bump Mockito to 5.13.0