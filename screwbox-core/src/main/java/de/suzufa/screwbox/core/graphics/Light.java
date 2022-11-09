package de.suzufa.screwbox.core.graphics;

import java.util.List;

import de.suzufa.screwbox.core.Bounds;
import de.suzufa.screwbox.core.Percent;
import de.suzufa.screwbox.core.Vector;
import de.suzufa.screwbox.core.entities.systems.RenderLightSystem;

/**
 * Subsystem for creating and rendering light effects to the screen. All added
 * light sources and shadow casters are reseted every frame. To actually render
 * any light effect you have to call {@link #render()}. Easiest way to use is
 * the {@link RenderLightSystem}.
 */
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

    /**
     * Adds an area to the {@link World} that is fully illuminated.
     * 
     * @param area the fully illuminated area
     */
    Light addFullBrightnessArea(Bounds area);

    /**
     * Sets the brightness of the {@link #ambientLight()} that illuminates the
     * {@link World} even without a lightsource.
     * 
     * @param ambientLight the brightness of the {@link #ambientLight()}.
     */
    Light setAmbientLight(Percent ambientLight);

    /**
     * Returns the brightness that illuminates the {@link World} even without a
     * lightsource.
     * 
     * @return brightness
     */
    Percent ambientLight();

    /**
     * Returns a list off all shadow casting areas.
     * 
     * @return shadow casting areas
     */
    List<Bounds> shadowCasters();

    /**
     * Renders the lightmap to {@link Window}. Can be automated by using
     * {@link RenderLightSystem}.
     */
    Light render();

}
