### 🚀 Features & improvements

- Retrieve a canvas for drawing directly on fames using `Frame.canvas()` (#736)
- Expanded lights now support rounded edges (737)
- Expanded lights now support fade out effect (738)
- Create new empty frames using `Frame.empty(Size)`
- Customize smoothing of expanded glow lens flares
- Adjust graphic content to the current resolution using `Graphics.resolutionScale()`

### 🪛 Bug Fixes

- Fixed wrong opacity when drawing rectangles

### 🧽 Cleanup & refactoring

- Renderer doesn't store last used color anymore to allow mixing render with custom drawing code
- Allowed light map scales up to 16 to support running on higher resolutions

### 📦 Dependency updates

- ...