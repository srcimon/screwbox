package io.github.srcimon.screwbox.core.environment.light;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.Environment;
import io.github.srcimon.screwbox.core.environment.Order;
import io.github.srcimon.screwbox.core.graphics.Light;

@Order(Order.SystemOrder.PRESENTATION_LIGHT)
public class LightRenderSystem implements EntitySystem {

    private static final Archetype CONE_LIGHTS = Archetype.ofSpacial(ConeLightComponent.class);
    private static final Archetype POINT_LIGHTS = Archetype.ofSpacial(PointLightComponent.class);
    private static final Archetype SPOT_LIGHTS = Archetype.ofSpacial(SpotLightComponent.class);
    private static final Archetype GLOWS = Archetype.ofSpacial(GlowComponent.class);
    private static final Archetype AERIAL_LIGHTS = Archetype.ofSpacial(AerialLightComponent.class);
    private static final Archetype SHADOW_CASTERS = Archetype.ofSpacial(ShadowCasterComponent.class);
    private static final Archetype ORTHOGRAPHIC_WALL = Archetype.ofSpacial(OrthographicWallComponent.class);

    @Override
    public void update(final Engine engine) {
        final Light light = engine.graphics().light();
        final Environment environment = engine.environment();

        // shadow casters
        for (final var entity : environment.fetchAll(SHADOW_CASTERS)) {
            final var shadow = entity.get(ShadowCasterComponent.class);
            light.addShadowCaster(entity.bounds(), shadow.selfShadow);
        }

        // orthographic walls
        for (final var entity : environment.fetchAll(ORTHOGRAPHIC_WALL)) {
            light.addOrthographicWall(entity.bounds());
        }

        // aerial lights
        for (final var entity : environment.fetchAll(AERIAL_LIGHTS)) {
            light.addAerialLight(entity.bounds(), entity.get(AerialLightComponent.class).color);
        }

        // cone lights
        for (final Entity entity : environment.fetchAll(CONE_LIGHTS)) {
            final var coneLight = entity.get(ConeLightComponent.class);
            light.addConeLight(entity.position(), coneLight.direction, coneLight.cone, coneLight.radius, coneLight.color);
        }

        // point lights
        for (final Entity entity : environment.fetchAll(POINT_LIGHTS)) {
            final var pointLight = entity.get(PointLightComponent.class);
            light.addPointLight(entity.position(), pointLight.radius, pointLight.color);
        }

        // spot lights
        for (final Entity entity : environment.fetchAll(SPOT_LIGHTS)) {
            final var spotLight = entity.get(SpotLightComponent.class);
            light.addSpotLight(entity.position(), spotLight.radius, spotLight.color);
        }

        // glows
        for (final Entity entity : environment.fetchAll(GLOWS)) {
            final var glow = entity.get(GlowComponent.class);
            light.addGlow(entity.position(), glow.radius, glow.color);
        }

        light.render();
    }
}