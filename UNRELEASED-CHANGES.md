### 🚀 Features & improvements

- Add randomness to sound playback options (#643)
- Ability to identify viewport at specified offset
- New assets `SpriteBunde.DOT_WHITE` and `ParticleBundle.SMOKE_TRAIL`
- Support logging messages with placeholders
- Documented `Particles` (#541)
- Added new Ease functions `S_CURVE_IN` and `S_CURVE_OUT`
- Added noise generators for Perlin noise and fractal noise im preparation for terrain generation
- Added generation of Random using multiple seeds to `MathUtil`
- Added snapping to grid to `Bounds`, `Vector`, `Offset` and `ScreenBounds`

### 🪛 Bug Fixes

- Fine tuned fps targeting 

### 🧽 Cleanup & refactoring

- Clear archetype cache automatically when number of updates is bigger than actual cache (#637)
- Used randomness in `FluidEffectsComponent`
- Performance tuned `PixelateShader` (#579)

### 📦 Dependency updates

- Bump Docusaurus to 3.8.0
- Bump NPM to 11.4.1
- Bump Node dependencies
- Bump JUnit to 5.13.0