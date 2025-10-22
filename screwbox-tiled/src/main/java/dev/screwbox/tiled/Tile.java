package dev.screwbox.tiled;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.graphics.Sprite;
public record Tile(Sprite sprite, Bounds bounds, Layer layer, Properties properties) {

}