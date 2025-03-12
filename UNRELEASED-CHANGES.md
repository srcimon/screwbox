### 🚀 Features & improvements

- Added `CHROMATIC_ABERRATION` shader (#513)
- Added `DISSOLVE` shader (#523)
- Added `IRIS_SHOT` shader
- Shader for updating ease value of underlying shader
- Set random shader offset for particle emitters
- Documented window module (#543)
- Overlay shader can be ignored for single drawing tasks
- Minor JavaDoc improvements
- Added multiple new methods to `Color`: `invert`, `rgb`, `alpha`, `brightness` and `greyscale`

### 🪛 Bug Fixes

- Fixed random offset for shaders
- Fixed broken saving mechanism when using shaders
- Fixed wrong opacity value when parsing hex values to color
- Fixed outline shader not working properly
- Fixed wrong block status of grid when using non square grids
- Fixed some minor typos in test names, internal methods and  JavaDoc

### 🧽 Cleanup & refactoring

- Inlined image filters that were only used in one shader
- Refactored shaders and image filters with new `Color` functionality

### 📦 Dependency updates

- Bump Mockito to 5.16.0
- Bump Node dependencies