### 🚀 Features & improvements

- Light from radial light sources creates indirect light when hitting an occluder (#1007)
- Configure indirect light using `GraphicsConfiguration`
- Prevent colors from getting lost when inverting opacity of image

### 🪛 Bug Fixes

- Fixed flickering lights when adding lights after starting light rendering
- Fixed graphics issue when using colored lights

### 🧽 Cleanup & refactoring

- Removed old workaround for flickering lights `Frame.nonGpuEnhancedFrame(...)`
- Slightly optimized `Vector`, `Angle`, `Line` and `Bounds` for performance
- Opened `Angle` constants for min/max values

### 📦 Dependency updates

- Bump Node dependencies