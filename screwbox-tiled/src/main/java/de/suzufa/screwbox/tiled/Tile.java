package de.suzufa.screwbox.tiled;

import de.suzufa.screwbox.core.Bounds;
import de.suzufa.screwbox.core.graphics.Sprite;

public interface Tile {

    Properties properties();

    Sprite sprite();

    Bounds renderBounds();

    Layer layer();
}