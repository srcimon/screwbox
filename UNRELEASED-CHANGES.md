### 🚀 Features & improvements

- Added components to use keyboard input for entity movement: `JumpControlComponent`, `LeftRightControlComponent` and `SuspendJumpComponent` (#493)
- Auto enable player controls using `Environment.enableControls()`
- Print a watermark with engine version to the screen (#352)
- New ease function `SQUARE_IN`, `SQUARE_OUT`
- Creating preview images for ease function
- Add borders to sprites and frames
- Exporting sprites as animated gif file
- Exporting single frames to png file
- Documented mouse subsystem
- Documented ease functions

### 🪛 Bug Fixes

- Fixed graphic glitches when changing split screen configuration

### 🧽 Cleanup & refactoring

- Added first tests to improve documentation quality
- Small performance tweak on retrieving frames from sprite
- Removed drawSpriteBatch from renderer
- Introduced `RenderPipeline` to hide renderer implementations
- Renamed split screen classes
- Made `Cache` serializable (even when it loses all content on deserialization)
- Optimized `Entity` serialization

### 📦 Dependency updates

- Bump JUnit to 5.12.0