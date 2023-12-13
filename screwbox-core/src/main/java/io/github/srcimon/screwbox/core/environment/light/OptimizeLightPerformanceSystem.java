package io.github.srcimon.screwbox.core.environment.light;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;
import io.github.srcimon.screwbox.core.utils.GeometryUtil;
import io.github.srcimon.screwbox.core.environment.*;

import java.util.List;
import java.util.Optional;

@Order(SystemOrder.OPTIMIZATION)
public class OptimizeLightPerformanceSystem implements EntitySystem {

    private static final Archetype COMBINABLES = Archetype.of(
            StaticLightBlockingComponent.class, LightBlockingComponent.class, TransformComponent.class);

    @Override
    public void update(final Engine engine) {
        final List<Entity> combinables = engine.environment().fetchAll(COMBINABLES);
        for (final var entity : combinables) {
            for (final var combinable : combinables) {
                if (tryToCombine(entity, combinable, engine)) {
                    return; // only one combination per frame
                }
            }
        }
        // at this point all light blockers have been combined
        for (final var entity : combinables) {
            entity.remove(StaticLightBlockingComponent.class);
        }
        engine.environment().remove(OptimizeLightPerformanceSystem.class);
    }

    private boolean tryToCombine(final Entity first, Entity second, final Engine engine) {
        if (first == second) {
            return false;
        }
        Optional<Bounds> result = GeometryUtil.tryToCombine(first.get(TransformComponent.class).bounds,
                second.get(TransformComponent.class).bounds);
        if (result.isPresent()) {
            Entity combined = new Entity()
                    .add(new LightBlockingComponent())
                    .add(new StaticLightBlockingComponent())
                    .add(new TransformComponent(result.get()));
            engine.environment().addEntity(combined);
            first.remove(StaticLightBlockingComponent.class);
            first.remove(LightBlockingComponent.class);
            second.remove(StaticLightBlockingComponent.class);
            second.remove(LightBlockingComponent.class);
            return true;
        }
        return false;
    }
}
