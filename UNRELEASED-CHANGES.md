### 🚀 Features & improvements

- Apply pressure to soft bodies
- Record collisions of soft bodies within `SoftBodyCollisionComponent`
- Preserve soft body shapes using `SoftBodyShapeComponent` (881)
- Create `Angle` from radians value

### 🪛 Bug Fixes

- Fixed soft bodies from getting stuck together because of zeroing velocity
- Fixed polygon bisector rays outside of polygon when testing certain shapes
- Fixed soft body ghost collisions
- Prevent infinite loops in `SoftBodySystem` when entities are linked to itself

### 🧽 Cleanup & refactoring

- Introduced automatically updated `shape` property in `SoftBodyComponent` and `RopeComponent` (#877)
- Added `Color.isVisible()` and used to clean up code
- Slightly improved performance of soft body collision resolve

### 📦 Dependency updates

- Bump Node dependencies