package de.suzufa.screwbox.core.graphics.light;

import java.util.List;

import de.suzufa.screwbox.core.Bounds;
import de.suzufa.screwbox.core.Percentage;
import de.suzufa.screwbox.core.Vector;
import de.suzufa.screwbox.core.graphics.Color;

//TODO: javadoc and tests
public interface Light {

    Light addPointLight(Vector position, final double range, final Color color);

    Light updateObstacles(List<Bounds> obstacles);

    Light setResolution(int resolution);

    int resolution();

    Light setUseAntialiasing(boolean useAntialiasing);

    Light setAmbientLight(Percentage ambientLight);

    Percentage ambientLight();

    boolean isUseAntialiasing();

    List<Bounds> obstacles();

    Light drawLightmap();

    Light setBlur(int blur);

    Light seal();

    // TODO: lens flares
    // TODO: anamorphic lens flares
}
