package de.suzufa.screwbox.tiled.internal;

import de.suzufa.screwbox.core.Bounds;
import de.suzufa.screwbox.core.graphics.Sprite;
import de.suzufa.screwbox.tiled.Layer;
import de.suzufa.screwbox.tiled.Properties;
import de.suzufa.screwbox.tiled.Tile;

public record DefaultTile(Sprite sprite, Bounds renderBounds, Layer layer, Properties properties) implements Tile {

}
