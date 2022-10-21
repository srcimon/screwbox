package de.suzufa.screwbox.core.entities.systems;

import java.util.ArrayList;
import java.util.List;

import de.suzufa.screwbox.core.Bounds;
import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.Vector;
import de.suzufa.screwbox.core.entities.Archetype;
import de.suzufa.screwbox.core.entities.Entity;
import de.suzufa.screwbox.core.entities.EntitySystem;
import de.suzufa.screwbox.core.entities.UpdatePriority;
import de.suzufa.screwbox.core.entities.components.LensFlareComponent;
import de.suzufa.screwbox.core.entities.components.LightObstacleComponent;
import de.suzufa.screwbox.core.entities.components.PointLightComponent;
import de.suzufa.screwbox.core.entities.components.SpotLightComponent;
import de.suzufa.screwbox.core.entities.components.TransformComponent;
import de.suzufa.screwbox.core.graphics.Light;

public class CreateLightSystem implements EntitySystem {

    private static final Archetype POINTLIGHT_EMITTERS = Archetype.of(
            PointLightComponent.class, TransformComponent.class);

    private static final Archetype SPOTLIGHT_EMITTERS = Archetype.of(
            SpotLightComponent.class, TransformComponent.class);

    private static final Archetype LENSFLARE_EMITTERS = Archetype.of(
            LensFlareComponent.class, TransformComponent.class);

    private static final Archetype OBSTACLES = Archetype.of(
            LightObstacleComponent.class, TransformComponent.class);

    @Override
    public void update(Engine engine) {
        Light light = engine.graphics().light();
        final List<Bounds> obstacles = new ArrayList<>();
        for (final var obstacle : engine.entities().fetchAll(OBSTACLES)) {
            obstacles.add(obstacle.get(TransformComponent.class).bounds);
        }
        light.updateObstacles(obstacles);

        for (final Entity pointLightEntity : engine.entities().fetchAll(POINTLIGHT_EMITTERS)) {
            final var pointLight = pointLightEntity.get(PointLightComponent.class);
            final Vector position = pointLightEntity.get(TransformComponent.class).bounds.position();
            light.addPointLight(position, pointLight.range, pointLight.color);
        }

        for (final Entity spotLightEntity : engine.entities().fetchAll(SPOTLIGHT_EMITTERS)) {
            final var spotLight = spotLightEntity.get(SpotLightComponent.class);
            final Vector position = spotLightEntity.get(TransformComponent.class).bounds.position();
            light.addSpotLight(position, spotLight.range, spotLight.color);
        }
        for (final Entity lensFlareEntity : engine.entities().fetchAll(LENSFLARE_EMITTERS)) {
            final var lensFlare = lensFlareEntity.get(LensFlareComponent.class);
            final Vector position = lensFlareEntity.get(TransformComponent.class).bounds.position();
            light.addLensFlare(position, lensFlare.size, lensFlare.color);
        }
        light.seal();
    }

    @Override
    public UpdatePriority updatePriority() {
        return UpdatePriority.PREPARATION;
    }

}
