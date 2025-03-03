### 🚀 Features & improvements

- Configure an overlay shader for all drawing operations on screen (#520)
- More configuration possibilities for water reflection effects
- More configuration possibilities for color outline shader
- Create shader previews using backgrounds
- Added new shaders to asset bundle: `WATER_COMICAL` and `OUTLINE_PULSE_WHITE`
- Ability to add shader to `ParticleOptions`
- Added `ShaderSetup.randomOffset()`

### 🪛 Bug Fixes

- Fixed typos in parameter names
- Fixed graphic glitches when using water reflections
- Fixed color out of range bug when applying `ColorizeShader` using certain base color

### 🧽 Cleanup & refactoring

- Renamed `DistortionShader`
- Renamed `ImageOperations`
- Renamed bundled shaders

### 📦 Dependency updates

- Bump Jackson to 2.18.3
- Bumped Node dependencies