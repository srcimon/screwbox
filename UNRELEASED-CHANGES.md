### 🚀 Features & improvements

- Added neon shader (#692)
- Added underwater shader (#687)
- Added util for stacking images
- Added `Sprite.firstFrame()`
- Added `Vector.reduce(double)`
- Significantly improved performance for common drawing calculations
- Support for size expansion and compact of shadow casters

### 🪛 Bug Fixes

- Fixed index out of bounds in `FloatSystem`
- Removed friction from `PhysicsComponent` because of feature duplication

### 🧽 Cleanup & refactoring

- Slightly improved performance of Perlin Noise
- Reworked `AirFrictionSystem` to use velocity instead of separate x and y axis values
- Renamed `PhysicsComponent.velocity`
- Renamed `ImageOperations`
- Renamed `rFrictionSystem` 
- Renamed `Rotation.ofVector(Vector)`

### 📦 Dependency updates

- Bump AssertJ to 3.27.4
- Bump Mockito to 5.19.0
- Bump Node dependencies