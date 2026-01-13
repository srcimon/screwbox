### 🚀 Features & improvements

- Added cloth soft body and cloth rendering (#903)
- Create soft bodies and ropes using `SoftPhysicsSupport` (#901)
- Added surface drawing to fluid rendering
- Easy resizing of `Bounds` and `Entity`
- Added functions to deal with optional parameters of entities: `Entity.forceId()`, `Entity.tryGet(Class)`
- Get closed version of `Polygon`
- Calculate area of `Polygon`
- Added `Size.outline()` and `.isOutline(Offset)`
- Entities can be tagged with keywords
- Frames use color cache for improved color picking performance
- Added `Frame.invalidateColorCache()`
- Adjust brightness of `Color`

### 🪛 Bug Fixes

- Fixed soft body collision glitches
- Fixed false negative offsets within `Grid`
- Fixed some possible NPEs in `SoftBodyCollisionSystem`

### 🧽 Cleanup & refactoring

- Fixed 8 spaces indents in code examples (#891)
- Added polygon draw function in `World`
- Slightly improved rope rendering performance
- Added caching for shoelace sum in `Polygon`
- Renamed `Polygon.isClockwise()`
- Renamed `Size.all()`
- Renamed `Frame.invalidateShaderCache()`

### 📦 Dependency updates

- Bump JUnit to 6.0.2