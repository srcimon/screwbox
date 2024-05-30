package io.github.srcimon.screwbox.core.environment.light;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.Order;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;
import io.github.srcimon.screwbox.core.utils.GeometryUtil;

import java.util.List;
import java.util.Optional;

@Order(Order.SystemOrder.OPTIMIZATION)
public class OptimizeLightPerformanceSystem implements EntitySystem {

    private static final Archetype COMBINABLES = Archetype.of(
            StaticShadowCasterComponent.class, ShadowCasterComponent.class, TransformComponent.class);

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
            entity.remove(StaticShadowCasterComponent.class);
        }
        engine.environment().remove(OptimizeLightPerformanceSystem.class);
    }

    private boolean tryToCombine(final Entity first, Entity second, final Engine engine) {
        if (first == second) {
            return false;
        }
        if (first.get(ShadowCasterComponent.class).selfShadow != second.get(ShadowCasterComponent.class).selfShadow) {
            return false;
        }
        Optional<Bounds> result = GeometryUtil.tryToCombine(first.get(TransformComponent.class).bounds,
                second.get(TransformComponent.class).bounds);
        if (result.isPresent()) {
            Entity combined = new Entity()
                    .add(new ShadowCasterComponent())
                    .add(new StaticShadowCasterComponent())
                    .add(new TransformComponent(result.get()));
            engine.environment().addEntity(combined);
            first.remove(StaticShadowCasterComponent.class);
            first.remove(ShadowCasterComponent.class);
            second.remove(StaticShadowCasterComponent.class);
            second.remove(ShadowCasterComponent.class);
            return true;
        }
        return false;
    }
}
