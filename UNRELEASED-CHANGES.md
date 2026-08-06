### 🚀 Features & improvements

- Added `NettrunnerSmokeStyle` and refined `FireSmokeStyle`
- Wind smoothly adjusts velocity instead of enforcing it (#1059)

### 🪛 Bug Fixes

- Fixed state for mouse button when down, leaving and re-entering the game window

### 🧽 Cleanup & refactoring

- Resuse smoke rendering images for reduced GC overhead (#1047)
- Skip smoke processing when no smoke present for massive performance gain (#1055)
- Replaced `Smoke.pinVelocity(...)` with `.approachTargetVelocity(...)`

### 📦 Dependency updates

- Bump frontend-maven-plugin to 2.0.2
- Bump maven-jar-plugin to 3.5.1
- Bump jacoco-maven-plugin to 0.8.15
- Bump ntral-publishing-maven-plugin 0.10.0