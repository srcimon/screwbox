# Noteworthy utils

List of utils that might be helpful when programming with ScrewBox.
For detailed info please use
the [JavaDoc](https://javadoc.io/doc/dev.screwbox/screwbox-core/latest/io/github/srcimon/screwbox/core/utils/package-summary.html).

| Util           | Description                                                                                  |
|----------------|----------------------------------------------------------------------------------------------|
| `TileMap`      | A simple way to import entities into the `Environment` directly from a string or an image.   |
| `Cache`        | Simple cache implementation used to store values that are expensive to retrieve.             |
| `ListUtil`     | Utility for list operations like merging, cross checking or retrieving random values.        |
| `Noise`        | Generating a noise value that switches back and forth between values with random inaccuracy. |
| `PerlinNoise`  | A Perlin Noise generator.                                                                    |
| `LazyValue`    | Wrapper for values that will be loaded only when needed to save precious cpu.                |
| `FractalNoise` | A fractal noise generator.                                                                   |
| `Scheduler`    | A simple scheduler that can trigger an action every so and so often.                         |
| `TimeoutCache` | A cache that uses dedicated timeouts for each value.                                         |
| `Validate`     | A helper for validating values.                                                              |
| `SmoothValue`  | Calculate a smoothed value based on x samples of an actual value. E.g. smoothed fps count.   |