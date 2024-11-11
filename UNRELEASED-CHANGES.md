# 2.6.0

### 🚀 Features & improvements

- Added methods for explizitly rendering `Ui.renderMenu()` and `Scenes.renderTransition()`, so drawing order of these subsystems can be customized (#416)
- Added new entity systems to automate rendering of menus, scene transistions and split screen borders (#416)
- Added friction support for physics entities
- Added option to specify background color of screen in `GraphicsConfiguration` (#425)
- Added support for recording text via keyboard

### 🪛 Bug Fixes

- Set min size to `Lightmap` to prevent crashes when viewports are too small
- Fixed wrong system order when adding systems using lambdas (#418)
- Avoid unneccesary duplicate drawing of split screen borders
- Added new validation for maximum values
- Fixed wrong pan of sounds in split screen mode

### 🧽 Cleanup & refactoring

- Renamed `SplitscreenOptions`
- Better names for `SystemOrders`
- Added missing JavaDoc to `SplitscreenOptions` and `GraphicsConfiguration`

### 📦 Dependency updates

- Bump Jackson to 2.18.1