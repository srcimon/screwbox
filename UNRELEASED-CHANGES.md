# 2.6.0

### 🚀 Features & improvements

- Added methods for explizitly rendering `Ui.renderMenu()`, `Scenes.renderSceneTransition()`,
  `Graphics renderSplitscreenBorders()` so drawing order of these subsystems can be customized
- Added new entity systems to automate rendering of menus, scene transistions and split screen borders
- Added option to draw split screens without borders.

### 🪛 Bug Fixes

- Set min size to `Lightmap` to prevent crashes when viewports are too small
- Fixed wrong system order when adding systems using lambdas (#418)

### 🧽 Cleanup & refactoring

- Renamed `SplitscreenOptions`
- Better names for `SystemOrders`
- Added JavaDoc to `SplitscreenOptions`

### 📦 Dependency updates

- ...