package io.github.srcimon.screwbox.core.ecosphere.systems;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.ecosphere.components.ShadowCasterComponent;
import io.github.srcimon.screwbox.core.ecosphere.components.StaticShadowCasterMarkerComponent;
import io.github.srcimon.screwbox.core.ecosphere.components.TransformComponent;
import io.github.srcimon.screwbox.core.utils.GeometryUtil;
import io.github.srcimon.screwbox.core.ecosphere.*;

import java.util.List;
import java.util.Optional;

@Order(SystemOrder.OPTIMIZATION)
public class CombineStaticShadowCastersSystem implements EntitySystem {

    private static final Archetype COMBINABLES = Archetype.of(
            StaticShadowCasterMarkerComponent.class, ShadowCasterComponent.class, TransformComponent.class);

    @Override
    public void update(final Engine engine) {
        final List<Entity> combinables = engine.ecosphere().fetchAll(COMBINABLES);
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
        engine.ecosphere().remove(CombineStaticShadowCastersSystem.class);
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
            engine.ecosphere().addEntity(combined);
            first.remove(StaticShadowCasterMarkerComponent.class);
            first.remove(ShadowCasterComponent.class);
            second.remove(StaticShadowCasterMarkerComponent.class);
            second.remove(ShadowCasterComponent.class);
            return true;
        }
        return false;
    }
}
