This release is truely audio focused.

### 🚀 Features & improvements

- Moved volume control to new `AudioConfiguration` (#186)
- Replaced `Audio.playEffectLooped(Sound)` with `Audio.playEffect(Sound, SoundOptions)` to make it extendable (#187)
- Replaced `Sound.fromMidi(byte[])` and `.fromWav(byte[])` with `.fromSoundData(byte[])`
- Added auto update from mono to stereo for `Sounds` on load to support additional audio features to come
- Added individual volume control for sounds via `SoundOptions.volume(Percent)`(#187)
- Added individual balance and pan control for sounds via `SoundOptions.pan(double)` and `.balance(double)` (#198)

### 🪛 Bug Fixes

- Fixed wrong volume when cached sound clip is played
- Fixed `Keyboard.isPressed(key)` is true if key is pressed for a longer duration (#194)

### 🧽 Cleanup & refactoring

- Renamed `Graphics.moveCameraTo(position)`
- Renamed `Sound.format()` to `.sourceFormat()` and splitted Formats in mono and stereo formats.

### 📦 Dependency updates

- Bump Junit to 5.10.2
- Bump AssertJ to 3.25.3
- Bump Mockito to 5.10.0