package io.github.srcimon.screwbox.tiled;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.graphics.Sprite;

public record Tile(Sprite sprite, Bounds renderBounds, Layer layer, Properties properties) {

}