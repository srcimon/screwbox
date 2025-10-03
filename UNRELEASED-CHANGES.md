### 🚀 Features & improvements

- Retrieve a canvas for drawing directly on fames using `Frame.canvas()` (#736)
- Expanded lights now support rounded edges (737)
- Expanded lights now support fade out effect (738)
- Create new empty frames using `Frame.empty(Size)`
- Customize smoothing of expanded glow lens flares
- Added option to let expanded lens flares rotate towards light source 
- Adjust graphic content to the current resolution using `Graphics.resolutionScale()`
- Configure absolute light quality instead of relative lightmap scale

### 🪛 Bug Fixes

- Fixed wrong opacity when drawing rectangles

### 🧽 Cleanup & refactoring

- Renderer doesn't store last used color anymore to allow mixing render with native drawing code
- Allow light map scales up to 64 times to support running performant on higher resolutions
- Avoid unnecessary resolution changes when already at target resolution
- Added constant for default resolution to `GraphicsConfiguration`
- Renamed `lightBlur` configuration property
- Added JavaDoc to `GraphicsConfigurationEvent`

### 📦 Dependency updates

- ...