### 🚀 Features & improvements

- Added support for vertical movement in `LeftRightControlComponent` and renamed to `MovementControlComponent` (#503)
- Retrieve axis sign value for two key input
- Added offset property to move backdrop shadows away from occluder

### 🪛 Bug Fixes

- ...

### 🧽 Cleanup & refactoring

- Renamed `DroppedFiles`
- Renamed `Keyboard.keyForAlias(Alias)`
- Made `Keyboard.forceKeyForAlias(Alias)` public
- Removed 500ms Thread.sleep() workaround when opening window on MacOs (#1142)
- Removing component from entity that is not present does not notify listeners anymore

### 📦 Dependency updates

- ...