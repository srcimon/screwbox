package dev.screwbox.core.environment.tweening;

import dev.screwbox.core.Engine;
import dev.screwbox.core.Line;
import dev.screwbox.core.Angle;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.EntitySystem;

/**
 * Updates the position of all {@link Entity}s that use tweening and have an {@link TweenPositionComponent} or {@link TweenOrbitPositionComponent}.
 */
public class TweenPositionSystem implements EntitySystem {

    private static final Archetype POSITION_TWEENS = Archetype.ofSpacial(TweenComponent.class, TweenPositionComponent.class);
    private static final Archetype ORBIT_TWEENS = Archetype.ofSpacial(TweenComponent.class, TweenOrbitPositionComponent.class);

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
            final var rotatedNormal = Angle.degrees(tweenEntity.get(TweenComponent.class).value.value() * 360).applyOn(normal);
            tweenEntity.moveTo(rotatedNormal.to());
        }
    }
}
