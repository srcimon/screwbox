package io.github.srcimon.screwbox.core.environment.light;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.Order;
import io.github.srcimon.screwbox.core.environment.SystemOrder;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;
import io.github.srcimon.screwbox.core.graphics.Light;

@Order(SystemOrder.PRESENTATION_LIGHT)
public class LightRenderSystem implements EntitySystem {

    private static final Archetype CONELIGHTS = Archetype.of(ConeLightComponent.class, TransformComponent.class);
    private static final Archetype POINTLIGHTS = Archetype.of(PointLightComponent.class, TransformComponent.class);
    private static final Archetype SPOTLIGHTS = Archetype.of(SpotLightComponent.class, TransformComponent.class);
    private static final Archetype GLOWS = Archetype.of(GlowComponent.class, TransformComponent.class);
    private static final Archetype SHADOWCASTERS = Archetype.of(LightBlockingComponent.class, TransformComponent.class);

    @Override
    public void update(final Engine engine) {
        final Light light = engine.graphics().light();

        for (final var shadowCaster : engine.environment().fetchAll(SHADOWCASTERS)) {
            light.addShadowCaster(shadowCaster.get(TransformComponent.class).bounds);
        }

        for (final Entity coneLightEntity : engine.environment().fetchAll(CONELIGHTS)) {
            final var coneLight = coneLightEntity.get(ConeLightComponent.class);
            final Vector position = coneLightEntity.get(TransformComponent.class).bounds.position();
            light.addConeLight(position, coneLight.direction, coneLight.cone, coneLight.radius, coneLight.color);
        }
        for (final Entity pointLightEntity : engine.environment().fetchAll(POINTLIGHTS)) {
            final var pointLight = pointLightEntity.get(PointLightComponent.class);
            final Vector position = pointLightEntity.get(TransformComponent.class).bounds.position();
            light.addPointLight(position, pointLight.radius, pointLight.color);
        }

        for (final Entity spotLightEntity : engine.environment().fetchAll(SPOTLIGHTS)) {
            final var spotLight = spotLightEntity.get(SpotLightComponent.class);
            final Vector position = spotLightEntity.get(TransformComponent.class).bounds.position();
            light.addSpotLight(position, spotLight.radius, spotLight.color);
        }

        for (final Entity glowEmitter : engine.environment().fetchAll(GLOWS)) {
            final var glow = glowEmitter.get(GlowComponent.class);
            final Vector position = glowEmitter.get(TransformComponent.class).bounds.position();
            light.addGlow(position, glow.radius, glow.color);
        }
        light.render();
    }
}