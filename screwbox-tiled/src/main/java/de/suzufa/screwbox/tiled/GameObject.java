package de.suzufa.screwbox.tiled;

import de.suzufa.screwbox.core.Bounds;
import de.suzufa.screwbox.core.Vector;

public interface GameObject {

    int id();

    String name();

    String type();

    Vector position();

    Bounds bounds();

    Properties properties();

    Layer layer();

}
