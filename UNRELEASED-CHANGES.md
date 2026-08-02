### 🚀 Features & improvements

- Added dynamic interactive smoke using real-time fluid dynamics
- Add relative position methods to `Offset`
- `Grid` can now store any data not just booleans (#791)
- More flexible `Grid` instantiation
- Ability to resize and move grids
- Allow non square grid cells

### 🪛 Bug Fixes

- Fixed white screen when starting a new game with postfilter enabled
- Fixed incorrect visible area size before opening the game window

### 🧽 Cleanup & refactoring

- Removed unused Grid methods
- Cleaned up `Grid` methods
- Reorganized graphics package with subpackages
- Moved methods for supported and current resolution from `Graphics` to `Screen`
- Simplified `Graphics`, `Light` and `Screen` update

### 📦 Dependency updates

- Bump Node dependencies
- Bump JUnit to 6.1.2
- Bump Docusaurus to 3.10.2