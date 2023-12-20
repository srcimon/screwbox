package io.github.srcimon.screwbox.core.environment.tweening;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;

/**
 * Updates the postion of all {@link Entity}s that use tweening and have an {@link TweenXPositionComponent} or {@link TweenYPositionComponent}.
 */
public class TweenPositionSystem implements EntitySystem {

    private static final Archetype X_TWEENS = Archetype.of(TweenComponent.class, TransformComponent.class, TweenXPositionComponent.class);
    private static final Archetype Y_TWEENS = Archetype.of(TweenComponent.class, TransformComponent.class, TweenYPositionComponent.class);

    @Override
    public void update(Engine engine) {
        for (final var tweenEntity : engine.environment().fetchAll(X_TWEENS)) {
            final var positionComponent = tweenEntity.get(TweenXPositionComponent.class);
            final var transformComponent = tweenEntity.get(TransformComponent.class);
            final var advance = (positionComponent.to - positionComponent.from) * tweenEntity.get(TweenComponent.class).value.value();
            transformComponent.bounds = transformComponent.bounds.moveTo(Vector.of(positionComponent.from + advance, transformComponent.bounds.position().y()));
        }

        for (final var tweenEntity : engine.environment().fetchAll(Y_TWEENS)) {
            final var positionComponent = tweenEntity.get(TweenYPositionComponent.class);
            final var transformComponent = tweenEntity.get(TransformComponent.class);
            final var advance = (positionComponent.to - positionComponent.from) * tweenEntity.get(TweenComponent.class).value.value();
            transformComponent.bounds = transformComponent.bounds.moveTo(Vector.of(transformComponent.bounds.position().x(), positionComponent.from + advance));
        }
    }
}
