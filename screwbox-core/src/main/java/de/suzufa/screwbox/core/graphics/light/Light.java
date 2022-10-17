package de.suzufa.screwbox.core.graphics.light;

import java.util.List;

import de.suzufa.screwbox.core.Bounds;
import de.suzufa.screwbox.core.Vector;
import de.suzufa.screwbox.core.graphics.Color;

public interface Light {

    Light addPointLight(Vector position, final double range, final Color color);

    Light updateObstacles(List<Bounds> obstacles);

    List<Bounds> obstacles();

    // TODO: lens flares
    // TODO: anamorphic lens flares
}
