### 🚀 Features & improvements

- Moved volume control to new `AudioConfiguration` (#186)
- Added `Audio.playEffect(Sound, SoundOptions)` and  `Audio.playMusic(Sound, SoundOptions)`

### 🪛 Bug Fixes

- Fixed wrong volume when cached sound clip is played
- Fixed `Keyboard.isPressed(key)` is true if key is pressed for a longer duration (#194)

### 🧽 Cleanup & refactoring

- Renamed `Graphics.moveCameraTo(position)`
- Removed `Audio.playEffectLooped(Sound)` and  `Audio.playMusicLooped(Sound)`

### 📦 Dependency updates

- Bump Junit to 5.10.2
- Bump AssertJ to 3.25.3
- Bump Mockito to 5.10.0