package dev.screwbox.core.environment.light;

import dev.screwbox.core.Engine;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.Environment;
import dev.screwbox.core.environment.ExecutionOrder;
import dev.screwbox.core.graphics.Light;

import static dev.screwbox.core.environment.Order.PRESENTATION_LIGHT;

@ExecutionOrder(PRESENTATION_LIGHT)
public class LightRenderSystem implements EntitySystem {

    private static final Archetype CONE_LIGHTS = Archetype.ofSpacial(ConeLightComponent.class);
    private static final Archetype CONE_GLOWS = Archetype.ofSpacial(ConeGlowComponent.class);
    private static final Archetype POINT_LIGHTS = Archetype.ofSpacial(PointLightComponent.class);
    private static final Archetype SPOT_LIGHTS = Archetype.ofSpacial(SpotLightComponent.class);
    private static final Archetype GLOWS = Archetype.ofSpacial(GlowComponent.class);
    private static final Archetype AREA_LIGHTS = Archetype.ofSpacial(AreaLightComponent.class);
    private static final Archetype AREA_GLOWS = Archetype.ofSpacial(AreaGlowComponent.class);
    private static final Archetype SHADOW_CASTERS = Archetype.ofSpacial(OccluderComponent.class);
    private static final Archetype ORTHOGRAPHIC_WALL = Archetype.ofSpacial(OrthographicWallComponent.class);

    @Override
    public void update(final Engine engine) {
        final Light light = engine.graphics().light();
        final Environment environment = engine.environment();

        // shadow casters
        for (final var entity : environment.fetchAll(SHADOW_CASTERS)) {
            final var shadow = entity.get(OccluderComponent.class);
            light.addOccluder(entity.bounds().expand(shadow.expand), shadow.isSelfOcclude);
        }

        // orthographic walls
        for (final var entity : environment.fetchAll(ORTHOGRAPHIC_WALL)) {
            light.addOrthographicWall(entity.bounds());
        }

        // area lights
        for (final var entity : environment.fetchAll(AREA_LIGHTS)) {
            final var areaLight = entity.get(AreaLightComponent.class);
            light.addAreaLight(entity.bounds(), areaLight.color, areaLight.curveRadius, areaLight.isFadeout);
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
            light.addGlow(entity.position(), glow.radius, glow.color, glow.lensFlare);
        }

        // cone glows
        for (final Entity entity : environment.fetchAll(CONE_GLOWS)) {
            final var glow = entity.get(ConeGlowComponent.class);
            light.addConeGlow(entity.position(), glow.direction, glow.cone, glow.radius, glow.color);
        }

        // area glows
        for (final Entity entity : environment.fetchAll(AREA_GLOWS)) {
            final var glow = entity.get(AreaGlowComponent.class);
            light.addAreaGlow(entity.bounds(), glow.radius, glow.color, glow.lensFlare);
        }

        light.render();
    }
}