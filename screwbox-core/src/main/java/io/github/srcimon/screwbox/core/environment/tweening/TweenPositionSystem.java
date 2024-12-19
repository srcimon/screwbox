package io.github.srcimon.screwbox.core.environment.tweening;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Line;
import io.github.srcimon.screwbox.core.Rotation;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;

/**
 * Updates the postion of all {@link Entity}s that use tweening and have an {@link TweenPositionComponent} or {@link TweenOrbitPositionComponent}.
 */
public class TweenPositionSystem implements EntitySystem {

    private static final Archetype POSITION_TWEENS = Archetype.of(TweenComponent.class, TransformComponent.class, TweenPositionComponent.class);
    private static final Archetype ORBIT_TWEENS = Archetype.of(TweenComponent.class, TransformComponent.class, TweenOrbitPositionComponent.class);

    @Override
    public void update(final Engine engine) {
        for (final var tweenEntity : engine.environment().fetchAll(POSITION_TWEENS)) {
            final var positionComponent = tweenEntity.get(TweenPositionComponent.class);
            final var advance = positionComponent.to.substract(positionComponent.from).multiply(tweenEntity.get(TweenComponent.class).value.value());
            tweenEntity.moveTo(positionComponent.from.add(advance));
        }

        for (final var tweenEntity : engine.environment().fetchAll(ORBIT_TWEENS)) {
            final var positionComponent = tweenEntity.get(TweenOrbitPositionComponent.class);
            final var normal = Line.normal(positionComponent.center, positionComponent.distance);
            final var rotatedNormal = Rotation.degrees(tweenEntity.get(TweenComponent.class).value.value() * 360).applyOn(normal);
            tweenEntity.moveTo(rotatedNormal.to());
        }
    }
}
