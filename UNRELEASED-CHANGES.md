### 🚀 Features & improvements

- Added directional light sources (#808)
- Calculate perpendicular lines

### 🪛 Bug Fixes

- Fixed missing hit detections for `Line.intersectionPoint(Line)` and `Line.intersects(Line)` when lines have same definition points
- Fixed blocked light sources not turned off when added at the wrong time (#930)

### 🧽 Cleanup & refactoring

- Slightly improved performance for finding nearest entity using raycasts
- Renamed `Angle.rotate(Line)`

### 📦 Dependency updates

- ...