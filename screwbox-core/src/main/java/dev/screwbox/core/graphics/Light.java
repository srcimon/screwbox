package dev.screwbox.core.graphics;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Percent;
import dev.screwbox.core.Rotation;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.Environment;
import dev.screwbox.core.environment.light.LightRenderSystem;
import dev.screwbox.core.environment.light.OrthographicWallComponent;

/**
 * Subsystem for creating and rendering light effects to the screen. All added
 * light sources and shadow casters are resetted every frame. To actually render
 * any light effect you have to call {@link #render()}. Easiest way to use is
 * the {@link LightRenderSystem}.
 */
public interface Light {

    /**
     * Adds a directed light to the {@link World}, that is affected by shadow casters.
     *
     * @param position  position of the light source in the map
     * @param direction the direction of the light
     * @param cone      the cone size of the light
     * @param radius    the radius of the light
     * @param color     the {@link Color} of the light
     */
    Light addConeLight(Vector position, Rotation direction, Rotation cone, double radius, Color color);

    /**
     * Adds a radial light source to the {@link World}, that is affected by shadow casters.
     *
     * @param position position of the light source in the map
     * @param radius   the radius of the light
     * @param color    the {@link Color} of the light
     */
    Light addPointLight(Vector position, double radius, Color color);

    /**
     * Adds a radial light source to the {@link World}, that is not affected by shadow casters.
     *
     * @param position position of the light source in the map
     * @param radius   the radius of the light
     * @param color    the {@link Color} of the light
     */
    Light addSpotLight(Vector position, double radius, Color color);

    /**
     * Adds object that cast shadows. Casts shadows over itself.
     *
     * @param shadowCaster the {@link Bounds} of the shadow caster
     * @see #addPointLight(Vector, double, Color) )
     * @see #addShadowCaster(Bounds, boolean)
     */
    default Light addShadowCaster(final Bounds shadowCaster) {
        return addShadowCaster(shadowCaster, true);
    }

    /**
     * Adds object that cast shadows.
     *
     * @param shadowCaster the {@link Bounds} of the shadow caster
     * @param selfShadow   specify if the object casts shadows over itself
     * @see #addPointLight(Vector, double, Color) )
     * @see #addShadowCaster(Bounds)
     */
    Light addShadowCaster(Bounds shadowCaster, boolean selfShadow);

    /**
     * Adds illumination to this area even when there are shadow casters at the same area. Used to support light effects
     * on orthographic walls. Can be automated by adding a {@link OrthographicWallComponent} to an {@link Entity}.
     *
     * @since 2.9.0
     */
    Light addOrthographicWall(Bounds bounds);

    /**
     * Adds an area to the {@link World} that is fully or partially illuminated.
     *
     * @param area  the fully illuminated area
     * @param color color used to illuminate the area
     * @since 2.18.0
     */
    Light addAerialLight(Bounds area, Color color);

    /**
     * Sets the brightness of the {@link #ambientLight()} that illuminates the
     * {@link World} even without a light source.
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
     * Will also add lens flares when enabled via {@link GraphicsConfiguration#isLensFlareEnabled()}.
     *
     * @param position  position of the glow effect
     * @param radius    radius of the glow effect
     * @param color     color of the glow effect
     * @param lensFlare lens flares caused by the glow
     * @since 3.8.0
     */
    Light addGlow(Vector position, double radius, Color color, LensFlare lensFlare);

    /**
     * Adds a glow effect to the given position. Can be combined with other light sources.
     * Will also add default lens flare effect when enabled via {@link GraphicsConfiguration#isLensFlareEnabled()}.
     *
     * @param position position of the glow effect
     * @param radius   radius of the glow effect
     * @param color    color of the glow effect
     */
    default Light addGlow(Vector position, double radius, Color color) {
        return addGlow(position, radius, color, new LensFlare());
    }

    /**
     * Renders the light map to all {@link Viewport viewports}. Can be automated by using {@link LightRenderSystem}.
     *
     * @see LightRenderSystem
     * @see Environment#enableLight()
     */
    Light render();

}
