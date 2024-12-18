### 🚀 Features & improvements

- Added custom game archivements (#270)
- New method `bounds(Bounds)` for conveniently adding a TransformComponent to an `Entity`
- Added archivements to platformer game example
- Added game assets `SpriteBundle.ARCHIVEMENT`, `SoundBundle.ARCHIVEMENT` and `Ease.IN_PLATEAU`

### 🪛 Bug Fixes

- Fixed sporadic graphic glitches when changing scenes
- Fixed stutter when scaling pixelfont text
- Fixed `SimpleUiLayouter` not considering offset of render area

### 🧽 Cleanup & refactoring

- Setup Mockito via `@MockitoSettings` (#441)
- Completed JavaDoc on `ScreenBounds` and `Ui`

### 📦 Dependency updates

- Bump Jackson to 2.18.2
- Bump Junit to 5.11.4