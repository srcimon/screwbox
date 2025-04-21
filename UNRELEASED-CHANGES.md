### 🚀 Features & improvements

- Create conveyor effect using `MotionShader`, `ShaderBundle.CONVEYOR` (#518)
- Added automatically updated depth property to `FloatComponent`
- Added lowering property to `FloatComponent` to adjust depth of objects in fluids

### 🪛 Bug Fixes

- Fixed missing wave detection when floating objects are near bottom of fluid
- Fixed serialization issue in `CollisionSensorComponent`
- Fixed rendering of last segment of smoothed polygons 
- Prevent fluids with less than two nodes

### 🧽 Cleanup & refactoring

- Updated default settings for `FluidInteractionComponent`

### 📦 Dependency updates

- ...