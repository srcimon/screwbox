### 🚀 Features & improvements

- Added separate length loss property to indirect light configuration
- Added glyphs to Boldzilla font `(`, `)`, `[`, `]`, `%`, `|` and `=`
- Rich text rendering using multiple fonts and shaders (#445)

### 🪛 Bug Fixes

- Fixed indirect light intensity config not working properly

### 🧽 Cleanup & refactoring

- Simplified scene transition code
- Renamed `TextOptions.shader(ShaderSetup)`
- Renamed `PixelFont` methods for consistency
- Added missing JavaDoc to `Entity`

### 📦 Dependency updates

- Bump Node dependencies
- Bump maven-dependency-plugin to 3.11.0
- Bump maven-enforcer-plugin to 3.6.3
- Bump maven-gpg-plugin to 3.2.8
- Bump maven-jar-plugin to 3.5.0
- Bump maven-release-plugin to 3.3.1
- Bump maven-surefire-plugin to 3.5.6
- Bump sonar-maven-plugin to 5.7.0.6970