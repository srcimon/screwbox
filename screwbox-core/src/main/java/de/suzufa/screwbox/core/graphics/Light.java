package de.suzufa.screwbox.core.graphics;

import java.util.List;

import de.suzufa.screwbox.core.Bounds;
import de.suzufa.screwbox.core.Percentage;
import de.suzufa.screwbox.core.Vector;

//TODO: javadoc and tests
public interface Light {

    // TODO: addNeonLight()
    // TODO: addLensFlare()
    // TODO: addAnamorphicLensFlare()

    Light addPointLight(Vector position, final double range, final Color color);

    Light addSpotLight(Vector position, final double range, final Color color);

    Light addLensFlare(Vector origin, double size);

    Light updateObstacles(List<Bounds> obstacles);

    Light setAmbientLight(Percentage ambientLight);

    Percentage ambientLight();

    List<Bounds> obstacles();

    Light drawLightmap();

    Light seal();
}
