### 🚀 Features & improvements

- Improved quality of soft body collisions using dampening

### 🪛 Bug Fixes

- Avoided possible NPEs in `SoftBodyOccludesSystem` and `RopeRenderSystem`

### 🧽 Cleanup & refactoring

- Replaced `CrtMonitorOverlaySystem` with `CrtMonitorPostFilter` (#957)
- Slightly improved performance of `SoftBodyCollisionSystem`
- Removed redundand steps from Github build action
- Removed redundand deploy from Github release to Maven Central action

### 📦 Dependency updates

- Bump Node dependencies
- Bump Node to 24
- Bump Mockito to 5.23.0