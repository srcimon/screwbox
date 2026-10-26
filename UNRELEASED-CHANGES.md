### 🚀 Features & improvements

- Fixed length property prevents length changes of ropes
- Added `Vector.clampDistance(Vector, double)`
- Significantly improved performance of texture fill (time consumed is reduced by 95%) (#1122)

### 🪛 Bug Fixes

- ...

### 🧽 Cleanup & refactoring

- Simplyfied validation for boolean conditions
- Updated graphics related classes with `BufferImage` instead of `Image` to reduce conversion overhead 
- Removed no longer necessary buffer image conversion
- Renamed `TextureFillOptions`

### 📦 Dependency updates

- Bump maven-surefire-plugin to 3.6.1