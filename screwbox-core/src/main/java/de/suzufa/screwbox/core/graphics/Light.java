package de.suzufa.screwbox.core.graphics;

import java.util.List;

import de.suzufa.screwbox.core.Bounds;
import de.suzufa.screwbox.core.Percent;
import de.suzufa.screwbox.core.Vector;

//TODO: javadoc and tests
public interface Light {

    /**
     * Adds a pointlight to the {@link World}. Pointlights cast shadows when
     * colliding with {@link #shadowCasters()}.
     * 
     * @param position position of the lightsource in the map
     * @param options  {@link LightOptions} of the lightsource
     */
    Light addPointLight(Vector position, LightOptions options);

    /**
     * Adds a spotlight to the {@link World}. Spotlights don't cast any shadow.
     * 
     * @param position position of the lightsource in the map
     * @param options  {@link LightOptions} of the lightsource
     */
    Light addSpotLight(Vector position, LightOptions options);

    /**
     * Adds objects that cast shadows.
     * 
     * @param shadowCasters the {@link Bounds} of the shadow casters
     * @see #addPointLight(Vector, LightOptions)
     */
    Light addShadowCasters(List<Bounds> shadowCasters);

    Light addFullBrightnessArea(Bounds area);

    Light setAmbientLight(Percent ambientLight);

    Percent ambientLight();

    List<Bounds> shadowCasters();

    Light render();

}
