# 2.6.0

### 🚀 Features & improvements

- Added methods for explizitly rendering `Ui.renderMenu()`, `Scenes.renderTransition()`,
  `Graphics renderSplitscreenBorders()` so drawing order of these subsystems can be customized (#416)
- Added new entity systems to automate rendering of menus, scene transistions and split screen borders (#416)
- Added option to draw split screens without borders
- Added friction support for Physics entities 

### 🪛 Bug Fixes

- Set min size to `Lightmap` to prevent crashes when viewports are too small
- Fixed wrong system order when adding systems using lambdas (#418)
- Reduced unneccesary duplicate drawing of split screen borders

### 🧽 Cleanup & refactoring

- Renamed `SplitscreenOptions`
- Better names for `SystemOrders`
- Added JavaDoc to `SplitscreenOptions`

### 📦 Dependency updates

- Bump Jackson to 2.18.1