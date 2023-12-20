package io.github.srcimon.screwbox.core.environment.tweening;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Line;
import io.github.srcimon.screwbox.core.Rotation;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.*;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;

/**
 * Updates the postion of all {@link Entity}s that use tweening and have an {@link TweenXPositionComponent} or {@link TweenYPositionComponent}.
 */
@Order(SystemOrder.PRESENTATION_UI)//TODO REMOVE
public class TweenPositionSystem implements EntitySystem {

    private static final Archetype X_TWEENS = Archetype.of(TweenComponent.class, TransformComponent.class, TweenXPositionComponent.class);
    private static final Archetype Y_TWEENS = Archetype.of(TweenComponent.class, TransformComponent.class, TweenYPositionComponent.class);
    private static final Archetype CIRCLE_TWEENS = Archetype.of(TweenComponent.class, TransformComponent.class, TweenCircleAroundPositionComponent.class);

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

        for (final var tweenEntity : engine.environment().fetchAll(CIRCLE_TWEENS)) {
            final var positionComponent = tweenEntity.get(TweenCircleAroundPositionComponent.class);
            final var transformComponent = tweenEntity.get(TransformComponent.class);
            var progress = tweenEntity.get(TweenComponent.class).value.value();
            var normal = Line.between(positionComponent.center, positionComponent.center.addY(positionComponent.distance));
            Line rotatedNormal = Rotation.degrees(progress * 360).applyOn(normal);
            var newPos = rotatedNormal.to();
            transformComponent.bounds = transformComponent.bounds.moveTo(newPos);
        }
    }
}
