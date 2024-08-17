### 🚀 Features & improvements

- Added support for changing audio playback speed `SoundOptions.speed()` (#364)
- Added `Audio.completedPlaybackCount()` and `.soundsPlayedCount()`
- Added `FixedRotationSystem` / `Component`
- Added methods to apply fixed rotation on `ParticleOptions`
- Added `SoundBundle.WATER`

### 🪛 Bug Fixes

- Fixed crash when adding a `ParticleEmitterComponent` to an entity without `TransformComponent`
- Cursor changes take effect immediatly

### 🧽 Cleanup & refactoring

- Changing window title only happens when text is changed
- Reduced overhead when creating particles
- Renamed `MovementRotationSystem`

### 📦 Dependency updates

- ...