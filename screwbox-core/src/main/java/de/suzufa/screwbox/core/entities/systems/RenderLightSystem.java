package de.suzufa.screwbox.core.entities.systems;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.Vector;
import de.suzufa.screwbox.core.entities.Archetype;
import de.suzufa.screwbox.core.entities.Entity;
import de.suzufa.screwbox.core.entities.EntitySystem;
import de.suzufa.screwbox.core.entities.UpdatePriority;
import de.suzufa.screwbox.core.entities.components.PointLightComponent;
import de.suzufa.screwbox.core.entities.components.ShadowCasterComponent;
import de.suzufa.screwbox.core.entities.components.SpotLightComponent;
import de.suzufa.screwbox.core.entities.components.TransformComponent;
import de.suzufa.screwbox.core.graphics.Light;

public class RenderLightSystem implements EntitySystem {

    private static final Archetype POINTLIGHT_EMITTERS = Archetype.of(
            PointLightComponent.class, TransformComponent.class);

    private static final Archetype SPOTLIGHT_EMITTERS = Archetype.of(
            SpotLightComponent.class, TransformComponent.class);

    private static final Archetype SHADOW_CASTERS = Archetype.of(
            ShadowCasterComponent.class, TransformComponent.class);

    @Override
    public void update(Engine engine) {
        Light light = engine.graphics().light();
        for (final var shadowCaster : engine.entities().fetchAll(SHADOW_CASTERS)) {
            light.addShadowCaster(shadowCaster.get(TransformComponent.class).bounds);
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

    @Override
    public UpdatePriority updatePriority() {
        return UpdatePriority.PRESENTATION_LIGHT;
    }
}