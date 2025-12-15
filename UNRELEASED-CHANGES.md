### 🚀 Features & improvements

- Apply pressure to soft bodies

### 🪛 Bug Fixes

- Fixed soft bodies from getting stuck together because of zeroing velocity
- Prevent infinite loops in `SoftBodySystem` when entities are linked to itself

### 🧽 Cleanup & refactoring

- Introduced automatically updated `shape` property in `SoftBodyComponent` and `RopeComponent` (#877)
- Added `Color.isVisible()` and used to clean up code

### 📦 Dependency updates

- Bump Node dependencies