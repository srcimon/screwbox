### 🚀 Features & improvements

- Limit entity speed using `PhysicsComponent.maximumVelocity`
- Added `Vector.cap(double)`
- Configure maximum rotation speed when using `MotionRotationComponent` (#797)
- Documented navigation (#542)

### 🪛 Bug Fixes

- Fixed `ConcurrentModificationException` in light rendering introduced in version 3.10.0

### 🧽 Cleanup & refactoring

- Renamed `MotionRotationComponent`
- Renamed ui interfaces and methods
- Removed `ColliderComponent` from navigation search and raycasting api default filter
- Added missing JavaDoc to navigation (#777)

### 📦 Dependency updates

- Bump actions/setup-node to v6
- Bump crazy-max/ghaction-import-gpg to v6.3.0