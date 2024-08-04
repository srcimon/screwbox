### 🚀 Features & improvements

- Completly reworked audio playback to remove any lag on playing sounds (#359)
- Added methods for changing already active audio playback
- Added `SoundComponent`/`System` to add a constant sound to an entity
- Added support for multiline text rendering
- Added `TextUtil.lineWrap(String, int)`
- Added `Async.taskCount()`
- Added `Size.expand(int)`
- Added `SoundBundle.NOTIFY`
- Added `Ease.IN_PLATEAU_OUT`

### 🪛 Bug Fixes

- Rework of audio fixes the unbearable results when using bluetooth headphones

### 🧽 Cleanup & refactoring

- Removed uneffective caching of audio clips
- Added missing validation for `LineDrawOptions.srokeWidth()`
- Added `Validate` to clean up validation code

### 📦 Dependency updates

- ...