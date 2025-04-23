### 🚀 Features & improvements

- Create conveyor effect using `MotionShader`, `ShaderBundle.CONVEYOR` (#518)
- Added automatically updated depth property to `FloatComponent`
- Added dive property to `FloatComponent` to adjust dive depth of objects in fluids
- Support for custom sensor range in `CollisionSensorComponent`
- Added size to `AsciiMap.Block`

### 🪛 Bug Fixes

- Fixed missing wave detection when floating objects are near bottom of fluid
- Fixed serialization issue in `CollisionSensorComponent`
- Fixed rendering of last segment of smoothed polygons 
- Prevent creating fluids with less than two nodes

### 🧽 Cleanup & refactoring

- Updated default settings for `FluidInteractionComponent`

### 📦 Dependency updates

- ...