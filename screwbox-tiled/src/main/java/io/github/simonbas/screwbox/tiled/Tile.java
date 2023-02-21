package io.github.simonbas.screwbox.tiled;

import io.github.simonbas.screwbox.core.Bounds;
import io.github.simonbas.screwbox.core.graphics.Sprite;

public record Tile(Sprite sprite, Bounds renderBounds, Layer layer, Properties properties) {

}