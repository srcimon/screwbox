### 🚀 Features & improvements

- Reduced microphone level latency from about 120ms to 10ms (#624)
- Add turbulence to fluids using `FluidTurbulenceComponent` (#626)
- Use `SpawnMode` for particles directly

### 🪛 Bug Fixes

- Fixed tiled package name
- Fixed failing audio in certain heavy load situations (#629)
- `SuspendJumpComponent` prevents jumping when diving

### 🧽 Cleanup & refactoring

- Moved Jackson dependency to `screwbox-tiled` (#619)
- Completed JavaDoc on `Environment`
- Limited SoundOptions.speed to 0.1 step values

### 📦 Dependency updates

- Bump Jackson to 2.19.0
- Bump Node dependencies
