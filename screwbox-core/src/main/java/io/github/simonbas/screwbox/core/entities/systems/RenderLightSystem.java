package io.github.simonbas.screwbox.core.entities.systems;

import io.github.simonbas.screwbox.core.Engine;
import io.github.simonbas.screwbox.core.Vector;
import io.github.simonbas.screwbox.core.entities.*;
import io.github.simonbas.screwbox.core.entities.components.*;
import io.github.simonbas.screwbox.core.graphics.Light;

@Order(SystemOrder.PRESENTATION_LIGHT)
public class RenderLightSystem implements EntitySystem {

    private static final Archetype CONELIGHT_EMITTERS = Archetype.of(
            ConeLightComponent.class, TransformComponent.class);

    private static final Archetype POINTLIGHT_EMITTERS = Archetype.of(
            PointLightComponent.class, TransformComponent.class);

    private static final Archetype SPOTLIGHT_EMITTERS = Archetype.of(
            SpotLightComponent.class, TransformComponent.class);

    private static final Archetype SHADOW_CASTERS = Archetype.of(
            ShadowCasterComponent.class, TransformComponent.class);

    @Override
    public void update(final Engine engine) {
        final Light light = engine.graphics().light();

        for (final var shadowCaster : engine.entities().fetchAll(SHADOW_CASTERS)) {
            light.addShadowCaster(shadowCaster.get(TransformComponent.class).bounds);
        }

        for (final Entity coneLightEntity : engine.entities().fetchAll(CONELIGHT_EMITTERS)) {
            final var coneLight = coneLightEntity.get(ConeLightComponent.class);
            final Vector position = coneLightEntity.get(TransformComponent.class).bounds.position();
            light.addConeLight(position, coneLight.direction, coneLight.cone, coneLight.options);
        }
        for (final Entity pointLightEntity : engine.entities().fetchAll(POINTLIGHT_EMITTERS)) {
            final var pointLight = pointLightEntity.get(PointLightComponent.class);
            final Vector position = pointLightEntity.get(TransformComponent.class).bounds.position();
            light.addPointLight(position, pointLight.options);
        }

        for (final Entity spotLightEntity : engine.entities().fetchAll(SPOTLIGHT_EMITTERS)) {
            final var spotLight = spotLightEntity.get(SpotLightComponent.class);
            final Vector position = spotLightEntity.get(TransformComponent.class).bounds.position();
            light.addSpotLight(position, spotLight.options);
        }
        light.render();
    }
}