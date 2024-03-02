### 🚀 Features & improvements

- Moved camera control to `Graphics.camera()`
- Added `CameraShake` (#211)
- Optimized drawing of rectangles using `RectangleDrawOptions` (#205)
- Optimized drawing of lines using `LineDrawOptions` (#206)
- Added methods for handling of singleton components and entitie to `Environment` (#219)
- Added `Graphics.moveCameraWithinVisualBounds(Vector, Bounds)` (#210)
- Added `ChaoticMovementEntity/System` (#214)
- Added `Vector.nearestOf(List<Vector>)`
- Added `Pixelfont.sizeOf(String)`
- Added `Line.intersections(List<Line>)`
- Added `Window.isOpen()`
- Added `Size.allPixels()`
- Added `Frame.listPixelDifferences(Frame)`
- Added `Color.hex(String)`
- Enhanced graphics of `AutomovementDebugSystem`

### 🪛 Bug Fixes

- Fixed `Window.size()` and `Screen.size()` when window has not been opened yet
- Initial `World.visibleArea()` drastically reduced via anticipated `Screen.size()`

### 🧽 Cleanup & refactoring

- Applied new naming schema for for optional and mandatory return values (#222)
- Renamed camera movement and zoom methods
- Renamed `PathfindingObstacleComponent`
- Renamed `Physics.updateGrid(Grid)`
- Moved screenshot taking from `Renderer` to `Screen`
- Simplified `CameraUpdateSystem`
- Added better error message when trying to take screenshot before window is opened
- Removed frame dependencies from renderer to enable render to draw on any graphics

### 📦 Dependency updates

- Bump Mockito to 5.11.0