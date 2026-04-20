### 🚀 Features & improvements

- Add boid motion (flocking) to entities (#987)
- `SpacialIndex` and `SpacialHashRegistry` allow ultra fast searches by position within lists of entities (#915)
- Added scene transition animation `BlackHoleAnimation`
- Added `Vector` functions to calculate dot product, normalized dot prodct
- Rotate and divide vectors
- Specify tolerated offset when comparing colors and frames
- Calculate next highest number of two value
- Find closest point within `Bounds` to specified point
- Scale `Bounds`

### 🪛 Bug Fixes

- Fixed black screen issue occuring in certain situations when using light

### 🧽 Cleanup & refactoring

- Improved performance of shockwave post effect by ignoring out of viewport waves (#964)
- Renamed `Border` constants

### 📦 Dependency updates

- Bump Jackson to 3.1.2 (#995)
- Bump Docusaurus to 3.10.0
- Bump actions/cache to v5
- Bump Node dependencies