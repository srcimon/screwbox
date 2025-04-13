package dev.screwbox.tiles;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.graphics.Sprite;

public record Tile(Sprite sprite, Bounds renderBounds, Layer layer, Properties properties) {

}