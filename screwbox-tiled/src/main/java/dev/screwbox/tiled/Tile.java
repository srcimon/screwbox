package dev.screwbox.tiled;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.graphics.Sprite;
//TODO rename renderBounds
public record Tile(Sprite sprite, Bounds renderBounds, Layer layer, Properties properties) {

}