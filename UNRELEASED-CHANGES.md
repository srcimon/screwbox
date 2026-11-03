### 🚀 Features & improvements

- Fixed length property prevents length changes of ropes
- Added `Vector.clampDistance(Vector, double)`
- Reduced texture fill processing time by 95% for significantly faster rendering (#1122)
- Significantly Improved color blending for colored lights

### 🪛 Bug Fixes

- Fixed indirect lights color bleed when using colored lights

### 🧽 Cleanup & refactoring

- Simplyfied validation for boolean conditions
- Updated graphics related classes with `BufferImage` instead of `Image` to reduce conversion overhead 
- Renamed `TextureFillOptions`

### 📦 Dependency updates

- Bump Node dependencies
- Bump maven-surefire-plugin to 3.6.1