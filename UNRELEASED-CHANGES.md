### 🚀 Features & improvements

- Added directional light sources (#808)
- Added `Vector.isSameAs(Vector)`
- Added `Line.move(Vector)` and `.length(double)`
- Calculate perpendicular lines
- Added rgb color extraction operations to `Color`
- Improved light rendering performance and quality

### 🪛 Bug Fixes

- Fixed missing hit detections for `Line.intersectionPoint(Line)` and `Line.intersects(Line)` when lines have same definition points
- Fixed blocked light sources not turned off when added at the wrong time (#930)
- Fixed inconsistent naming of lightmap blur configuration property

### 🧽 Cleanup & refactoring

- Slightly improved performance for finding nearest entity using raycasts
- Significantly improved image blurring performance
- Renamed `Angle.rotate(Line)` and `Angle.rotateAroundCenter(Vector, Vector)`

### 📦 Dependency updates

- ...