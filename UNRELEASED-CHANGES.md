### 🚀 Features & improvements

- Moved volume control to new `AudioConfiguration` (#186)
- Replaced `Audio.playEffectLooped(Sound)` with `.playEffect(Sound, SoundOptions)`, same for `.playMusicLooped`

### 🪛 Bug Fixes

- Fixed wrong volume when cached sound clip is played
- Fixed `Keyboard.isPressed(key)` is true if key is pressed for a longer duration (#194)

### 🧽 Cleanup & refactoring

- Renamed `Graphics.moveCameraTo(position)`

### 📦 Dependency updates

- Bump Junit to 5.10.2
- Bump AssertJ to 3.25.3
- Bump Mockito to 5.10.0