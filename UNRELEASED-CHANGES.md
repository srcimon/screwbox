## v1.0.0-RC3


### 🚀 Features & improvements

- Added javadoc to `Keyboard`
- Added `Environment.addEntity(id, components)` and `.addEntity(components)`
- Added `World.drawCircle()` (#140)

### 🪛 Bug Fixes

- ...

### 🧽 Cleanup & refactoring

- Renamed misleading `Entities` to `Environment` (#132)
- Moved all future ideas and plans from the readme to Github issues (#131)
- Renamed generic `Ecosphere.add()` method to specify if adding a system or an entity
- Renamed `Segment` to `Line`
- Renamed some `Keyboard` and `Mouse` methods to more specific names
- Renamed `Scene.initialize()` to `populate()`
- Renamed `Environment.allEntities()` to `entities()`

### 📦 Dependency updates

- Bump  mockito to 5.8.0