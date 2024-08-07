### 🚀 Features & improvements

- Completly reworked audio playback to remove any lag on playing sounds (#359)
- Allow changes of options for already playing sounds
- Added `SoundComponent`/`System` to add a constant sound to an entity
- Added `Environment.enableAudio()` to add audio related entity systems.
- Added support for multiline text rendering
- Added `Async.taskCount()`
- Added `TextUtil` for text related helper methods
- Added `Size.expand(int)`
- Added sound asset `SoundBundle.NOTIFY`
- Added `Ease.IN_PLATEAU_OUT`

### 🪛 Bug Fixes

- Rework of audio fixes the unbearable results when using bluetooth headphones

### 🧽 Cleanup & refactoring

- Removed uneffective caching of audio clips
- Added missing validation for `LineDrawOptions.srokeWidth()`
- Added `Validate` to clean up validation code

### 📦 Dependency updates

- ...