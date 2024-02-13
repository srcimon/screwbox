This release is truely audio focused.

### 🚀 Features & improvements

- Moved volume control to new `AudioConfiguration` (#186)
- Introduced `SoundOptions` to contain meta information for playbacks to make configuration extendable
- Merged `Audio.playMusic(...)` and `.playEffects(...)` to `.playSound(Sound, SoundOptions)` (#187)
- Added auto update from mono to stereo for `Sounds` on load to support additional audio features like pan and balance
- Replayed all effect and music specific audio methods with `Audio.playSound(Sound)` and `.play(Sound, SoundOptions)`
- Replaced `Sound.fromMidi(byte[])` and `.fromWav(byte[])` with `.fromSoundData(byte[])`
- Added individual volume control for sounds via `SoundOptions.volume(Percent)`(#187)
- Added individual balance and pan control for sounds via `SoundOptions.pan(double)` and `.balance(double)` (#198)
- Added `Audio.playSound(Sound, Vector)` to automatically calculate volume and pan based on distance and direction to camera. (#185)

### 🪛 Bug Fixes

- Fixed wrong volume when cached sound clip is played
- Fixed `Keyboard.isPressed(key)` is true if key is pressed for a longer duration (#194)

### 🧽 Cleanup & refactoring

- Renamed `Graphics.moveCameraTo(position)`
- Renamed `Sound.format()` to `.sourceFormat()` and splitted Formats in mono and stereo formats.
- Renamed `Graphics.updateZoom` and `.updateZoomBy`
- Renamed `Audio.stop(Sound)` to `.stopSound(Sound)`
- Renamed all example applications to ...App

### 📦 Dependency updates

- Bump Junit to 5.10.2
- Bump AssertJ to 3.25.3
- Bump Mockito to 5.10.0