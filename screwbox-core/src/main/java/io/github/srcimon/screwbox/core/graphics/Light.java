package io.github.srcimon.screwbox.core.graphics;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.Rotation;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.light.LightRenderSystem;
import io.github.srcimon.screwbox.core.window.Window;

import java.util.List;

/**
 * Subsystem for creating and rendering light effects to the screen. All added
 * light sources and shadow casters are reseted every frame. To actually render
 * any light effect you have to call {@link #render()}. Easiest way to use is
 * the {@link LightRenderSystem}.
 */
public interface Light {

    /**
     * Adds a directed light to the {@link World}. Cone lights cast shadows.
     *
     * @param position  position of the lightsource in the map
     * @param direction the direction of the light
     * @param cone      the cone size of the light
     * @param options   {@link LightOptions} of the lightsource
     */
    Light addConeLight(Vector position, Rotation direction, Rotation cone, LightOptions options);

    /**
     * Adds a pointlight to the {@link World}. Pointlights cast shadows.
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
     * Adds object that cast shadows.
     *
     * @param shadowCaster the {@link Bounds} of the shadow caster
     * @see #addPointLight(Vector, LightOptions)
     * @see #addShadowCasters(List)
     */
    Light addShadowCaster(Bounds shadowCaster);

    /**
     * Adds objects that cast shadows.
     *
     * @param shadowCasters the {@link Bounds} of the shadow casters
     * @see #addPointLight(Vector, LightOptions)
     * @see #addShadowCaster(Bounds)
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
     * Renders the lightmap to {@link Window}. Can be automated by using
     * {@link LightRenderSystem}.
     */
    Light render();

}
