### 🚀 Features & improvements

- Ability to retrieve blocks of more than one adjacent tile from `AsciiMap` (#586)
- Ability to retrieve tile at specific position within `AsciiMap`
- Added binary blend to `MaskImageFilter` in preparation for better scene transitions (#571)
- Separate horizontal and vertical friction in `FloatComponent`
- Control components allow jumping while floating
- Added surface property to `FuidComponent`

### 🪛 Bug Fixes

- Fixed floating objects not floating when reaching out of fluid
- Fixed issue with changing stroke width when rendering polygons
- Used non approximate position for floating entities to avoid imprecision (#603)

### 🧽 Cleanup & refactoring

- `AsciiMap.Tile` uses two dimensional size
- Lazy initialize `Path` segments to improve performance
- Added JavaDoc to `Path`

### 📦 Dependency updates

- Bump JUnit to 5.12.2
- Bump Node dependencies