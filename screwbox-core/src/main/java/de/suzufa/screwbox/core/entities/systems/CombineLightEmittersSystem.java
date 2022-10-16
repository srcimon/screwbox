package de.suzufa.screwbox.core.entities.systems;

import java.util.List;
import java.util.Optional;

import de.suzufa.screwbox.core.Bounds;
import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.entities.Archetype;
import de.suzufa.screwbox.core.entities.Entity;
import de.suzufa.screwbox.core.entities.EntitySystem;
import de.suzufa.screwbox.core.entities.UpdatePriority;
import de.suzufa.screwbox.core.entities.components.LightBlockingComponent;
import de.suzufa.screwbox.core.entities.components.StaticLightBlockerMarkerComponent;
import de.suzufa.screwbox.core.entities.components.TransformComponent;
import de.suzufa.screwbox.core.utils.GeometryUtil;

public class CombineLightEmittersSystem implements EntitySystem {

    private static final Archetype COMBINABLES = Archetype.of(
            StaticLightBlockerMarkerComponent.class, LightBlockingComponent.class, TransformComponent.class);

    @Override
    public void update(final Engine engine) {
        final List<Entity> combinables = engine.entities().fetchAll(COMBINABLES);
        for (final var entity : combinables) {
            for (final var combinable : combinables) {
                if (tryToCombine(entity, combinable, engine)) {
                    return; // only one combination per frame
                }
            }
        }
        // at this point all colliders have been combined
        for (final var entity : combinables) {
            entity.remove(StaticLightBlockerMarkerComponent.class);
        }
        engine.entities().remove(CombineLightEmittersSystem.class);
    }

    private boolean tryToCombine(final Entity first, Entity second, final Engine engine) {
        Optional<Bounds> result = GeometryUtil.tryToCombine(first.get(TransformComponent.class).bounds,
                second.get(TransformComponent.class).bounds);
        if (result.isPresent()) {
            Entity combined = new Entity()
                    .add(new LightBlockingComponent())
                    .add(new TransformComponent(result.get()));
            engine.entities().add(combined);
            first.remove(StaticLightBlockerMarkerComponent.class);
            first.remove(LightBlockingComponent.class);
            second.remove(StaticLightBlockerMarkerComponent.class);
            second.remove(LightBlockingComponent.class);
            return true;
        }
        return false;
    }

    @Override
    public UpdatePriority updatePriority() {
        return UpdatePriority.OPTIMITATION;
    }

}
