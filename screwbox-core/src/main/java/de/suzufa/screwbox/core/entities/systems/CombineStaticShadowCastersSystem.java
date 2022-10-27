package de.suzufa.screwbox.core.entities.systems;

import java.util.List;
import java.util.Optional;

import de.suzufa.screwbox.core.Bounds;
import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.entities.Archetype;
import de.suzufa.screwbox.core.entities.Entity;
import de.suzufa.screwbox.core.entities.EntitySystem;
import de.suzufa.screwbox.core.entities.UpdatePriority;
import de.suzufa.screwbox.core.entities.components.ShadowCasterComponent;
import de.suzufa.screwbox.core.entities.components.StaticShadowCasterMarkerComponent;
import de.suzufa.screwbox.core.entities.components.TransformComponent;
import de.suzufa.screwbox.core.utils.GeometryUtil;

public class CombineStaticShadowCastersSystem implements EntitySystem {

    private static final Archetype COMBINABLES = Archetype.of(
            StaticShadowCasterMarkerComponent.class, ShadowCasterComponent.class, TransformComponent.class);

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
        // at this point all light blockers have been combined
        for (final var entity : combinables) {
            entity.remove(StaticShadowCasterMarkerComponent.class);
        }
        engine.entities().remove(CombineStaticShadowCastersSystem.class);
    }

    private boolean tryToCombine(final Entity first, Entity second, final Engine engine) {
        if (first == second) {
            return false;
        }
        Optional<Bounds> result = GeometryUtil.tryToCombine(first.get(TransformComponent.class).bounds,
                second.get(TransformComponent.class).bounds);
        if (result.isPresent()) {
            Entity combined = new Entity()
                    .add(new ShadowCasterComponent())
                    .add(new StaticShadowCasterMarkerComponent())
                    .add(new TransformComponent(result.get()));
            engine.entities().add(combined);
            first.remove(StaticShadowCasterMarkerComponent.class);
            first.remove(ShadowCasterComponent.class);
            second.remove(StaticShadowCasterMarkerComponent.class);
            second.remove(ShadowCasterComponent.class);
            return true;
        }
        return false;
    }

    @Override
    public UpdatePriority updatePriority() {
        return UpdatePriority.OPTIMITATION;
    }

}
