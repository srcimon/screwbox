### 🚀 Features & improvements

- Full screen rotation does not cause fps drop anymore

### 🪛 Bug Fixes

- Fixed flickering lights in split screen mode
- Fixed flickering lights when using orhographic walls

### 🧽 Cleanup & refactoring

- Reduced time used by reflection image post filter by 70%
- Lightmaps are not longer created when light is not enabled
- Used constants for default options to reduce gc load (#943)
- Specified maven-install-plugin version
- Moved full screen rotation from renderer to screen (#946)
- Removed on screen canvas resizing and moving

### 📦 Dependency updates

- Bump frontend-maven-plugin to 2.0.0
- Bump Node dependencies
- Bump maven-source-plugin to 3.4.0
- Bump central-publishing-maven-plugin to 0.10.0