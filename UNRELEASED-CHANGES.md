### 🚀 Features & improvements

- Added `IRIS_SHOT` and `CHROMATIC_ABERRATION` shaders (#513)
- Shader for updating ease value of underlying shader
- Set random shader offset for particle emitters
- Documented window module (#543)
- Overlay shader can be ignored for single drawing tasks
- Minor JavaDoc improvements
- Added multiple new methods to `Color`: `invert`, `rgb`, `alpha` and `grayscale`

### 🪛 Bug Fixes

- Fixed random offset for shaders

### 🧽 Cleanup & refactoring

- Inlined image filters that were only used in one shader
- Refactored shaders and image filters with new `Color` functionality

### 📦 Dependency updates

- Bump Mockito to 5.16.0