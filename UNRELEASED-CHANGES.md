### 🚀 Features & improvements

- Added 13 more missing keys to `Keys`
- Add multiple entity systems via package name using `Environment.addSystemsFromPackage(package)`

### 🪛 Bug Fixes

- Prevent duplicate registration of entity systems in enviroment
- Fixed algorithm of finding default construtor

### 🧽 Cleanup & refactoring

- Renamved and moved `TargetMovementSystem` and `PathMovementSystem` to `Environment.enableAi()`
- Moved instance creation via default constructor to `Reflections`

### 📦 Dependency updates

- Bump AssertJ to 3.27.3