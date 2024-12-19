package io.github.srcimon.screwbox.core.environment.light;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.Order;
import io.github.srcimon.screwbox.core.graphics.Light;

@Order(Order.SystemOrder.PRESENTATION_LIGHT)
public class LightRenderSystem implements EntitySystem {

    private static final Archetype CONELIGHTS = Archetype.ofSpacial(ConeLightComponent.class);
    private static final Archetype POINTLIGHTS = Archetype.ofSpacial(PointLightComponent.class);
    private static final Archetype SPOTLIGHTS = Archetype.ofSpacial(SpotLightComponent.class);
    private static final Archetype GLOWS = Archetype.ofSpacial(GlowComponent.class);
    private static final Archetype SHADOWCASTERS = Archetype.ofSpacial(ShadowCasterComponent.class);

    @Override
    public void update(final Engine engine) {
        final Light light = engine.graphics().light();

        for (final var shadowCaster : engine.environment().fetchAll(SHADOWCASTERS)) {
            final var shadow = shadowCaster.get(ShadowCasterComponent.class);
            light.addShadowCaster(shadowCaster.bounds(), shadow.selfShadow);
        }

        for (final Entity coneLightEntity : engine.environment().fetchAll(CONELIGHTS)) {
            final var coneLight = coneLightEntity.get(ConeLightComponent.class);
            light.addConeLight(coneLightEntity.position(), coneLight.direction, coneLight.cone, coneLight.radius, coneLight.color);
        }
        for (final Entity pointLightEntity : engine.environment().fetchAll(POINTLIGHTS)) {
            final var pointLight = pointLightEntity.get(PointLightComponent.class);
            light.addPointLight(pointLightEntity.position(), pointLight.radius, pointLight.color);
        }

        for (final Entity spotLightEntity : engine.environment().fetchAll(SPOTLIGHTS)) {
            final var spotLight = spotLightEntity.get(SpotLightComponent.class);
            light.addSpotLight(spotLightEntity.position(), spotLight.radius, spotLight.color);
        }

        for (final Entity glowEmitter : engine.environment().fetchAll(GLOWS)) {
            final var glow = glowEmitter.get(GlowComponent.class);
            light.addGlow(glowEmitter.position(), glow.radius, glow.color);
        }

        light.render();
    }
}