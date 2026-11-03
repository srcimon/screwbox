### 🚀 Features & improvements

- Fixed length property makes rope lengths static
- Reduced texture fill processing time by 95% for significantly faster rendering (#1122)
- Significantly Improved color blending for colored lights
- Added `Vector.clampDistance(Vector, double)`
- Emit smoke from an area
- Added customization options for turbulence and area emit in `SmokeEmitterComponent` (#1069)

### 🪛 Bug Fixes

- Fixed indirect lights color bleed when using colored lights

### 🧽 Cleanup & refactoring

- Simplyfied validation for boolean conditions
- Updated graphics related classes with `BufferImage` instead of `Image` to reduce conversion overhead
- Added min and max method for three inputs
- Renamed `TextureFillOptions`

### 📦 Dependency updates

- Bump Node dependencies
- Bump maven-surefire-plugin to 3.6.1