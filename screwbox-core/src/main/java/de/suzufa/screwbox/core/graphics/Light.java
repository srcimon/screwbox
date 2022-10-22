package de.suzufa.screwbox.core.graphics;

import java.util.List;

import de.suzufa.screwbox.core.Bounds;
import de.suzufa.screwbox.core.Percentage;
import de.suzufa.screwbox.core.Vector;

//TODO: javadoc and tests
public interface Light {

    // TODO: addNeonLight()
    // TODO: addAnamorphicLensFlare()

    Light addPointLight(Vector position, final double range, final Color color);

    Light addSpotLight(Vector position, final double range, final Color color);

    Light addGlow(Vector origin, double size, Color color);

    Light updateObstacles(List<Bounds> obstacles);

    Light setAmbientLight(Percentage ambientLight);

    Percentage ambientLight();

    List<Bounds> obstacles();

    Light render();

    Light seal();

    boolean isSealed();

}
