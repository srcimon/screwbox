package de.suzufa.screwbox.core.graphics;

import java.util.List;

import de.suzufa.screwbox.core.Bounds;
import de.suzufa.screwbox.core.Percent;
import de.suzufa.screwbox.core.Vector;

//TODO: javadoc and tests
public interface Light {

    Light addPointLight(Vector position, LightOptions options);

    Light addSpotLight(Vector position, LightOptions options);

    Light updateShadowCasters(List<Bounds> shadowCasters);

    Light setAmbientLight(Percent ambientLight);

    Percent ambientLight();

    List<Bounds> shadowCasters();

    Light render();

    Light seal();

    boolean isSealed();

}
