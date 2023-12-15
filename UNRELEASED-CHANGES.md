## v1.0.0-RC3


### 🚀 Features & improvements

- Added `World.drawCircle()` (#140)
- Added `Environment.addEntity(id, components)` and `.addEntity(components)`
- Added `Key.fromCode(code)` and `Keyboard.pressedKeys()` and `.downKeys()`
- Added `Environment.addOrReplaceSystem(system)`
- Added / completed javadoc on `Keyboard`, `Duration`
- Replaced `FadeOutSystem`, `TimeOutSystem` with extendable `TweenSystem` (#147)
- Grouped environment classes in specific packages (#149)
- Added missing keys `N`, `COMMA`, `DOT`
- Added `Raycast.ray()`

### 🪛 Bug Fixes

- Fixed possiblity of calling `Light.render()` twice in a frame (#152)
- Fixed problems with plugin version via adding maven-enforcer-plugin

### 🧽 Cleanup & refactoring

- Moved api for saving and loading the game state into `Environment` (#133)
- Renamed misleading `Entities` to `Environment` (#132)
- Moved all future ideas and plans from the readme to Github issues (#131)
- Renamed generic `Ecosphere.add()` method to specify if adding a system or an entity
- Renamed light related components and systems
- Renamed `Segment` to `Line`
- Renamed some `Keyboard` and `Mouse` methods to more specific names
- Renamed `Scene.initialize()` to `populate()`
- Renamed `Environment.allEntities()` to `entities()`
- Removed `Duration.addNanos(nanos)`

### 📦 Dependency updates

- Bump Mockito to 5.8.0