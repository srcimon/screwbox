### 🚀 Features & improvements

- Completly reworked audio playback to remove any lag on playing sounds (#359)
- Allowed changes in volume and pan for already playing sounds
- Added `SoundComponent`/`System` to add a continuously repeated playback to an entity (#361)
- Added `Environment.enableAudio()` to add audio related entity systems
- Rendering now support multiline text rendering
- Added `Async.taskCount()`
- Added `TextUtil` for text related helper methods
- Added `Size.expand(int)`
- Added sound asset `SoundBundle.NOTIFY`
- Added `Ease.IN_PLATEAU_OUT`

### 🪛 Bug Fixes

- Rework of audio fixes the unbearable results when using bluetooth headphones

### 🧽 Cleanup & refactoring

- Added missing validation for `LineDrawOptions.srokeWidth()`
- Added `Validate` to clean up validation code

### 📦 Dependency updates

- ...