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
     * @param radius    the radius of the light
     * @param color     the {@link Color} of the light
     */
    Light addConeLight(Vector position, Rotation direction, Rotation cone, double radius, Color color);

    /**
     * Adds a pointlight to the {@link World}. Pointlights cast shadows.
     *
     * @param position position of the lightsource in the map
     * @param radius   the radius of the light
     * @param color    the {@link Color} of the light
     */
    Light addPointLight(Vector position, double radius, Color color);

    /**
     * Adds a spotlight to the {@link World}. Spotlights don't cast any shadow.
     *
     * @param position position of the lightsource in the map
     * @param radius   the radius of the light
     * @param color    the {@link Color} of the light
     */
    Light addSpotLight(Vector position, double radius, Color color);

    /**
     * Adds object that cast shadows.
     *
     * @param shadowCaster the {@link Bounds} of the shadow caster
     * @see #addPointLight(Vector, double, Color) )
     * @see #addShadowCasters(List)
     */
    Light addShadowCaster(Bounds shadowCaster);

    /**
     * Adds objects that cast shadows.
     *
     * @param shadowCasters the {@link Bounds} of the shadow casters
     * @see #addPointLight(Vector, double, Color)
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
     * Returns the current ambient light brightness.
     *
     * @return brightness
     */
    Percent ambientLight();

    /**
     * Adds a glow effect to the given position. Can be combined with other light sources.
     *
     * @param position position of the glow effect
     * @param radius   radius of the glow effect
     * @param color    color of the glow effect
     */
    Light addGlow(Vector position, double radius, Color color);

    //TODO changelog
    //TODO javadoc
    Light addBloom(Bounds area, Color color);

    /**
     * Renders the lightmap to {@link Window}. Can be automated by using
     * {@link LightRenderSystem}.
     */
    Light render();

}
