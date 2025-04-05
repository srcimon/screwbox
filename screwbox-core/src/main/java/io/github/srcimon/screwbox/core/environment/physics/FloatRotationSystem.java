package io.github.srcimon.screwbox.core.environment.physics;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Rotation;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.rendering.RenderComponent;

import java.util.Objects;

/**
 * Adjusts {@link Rotation} of {@link Entity entities} having {@link FloatRotationComponent}.
 */
public class FloatRotationSystem implements EntitySystem {

    private static final Archetype FLOATINGS = Archetype.ofSpacial(FloatComponent.class, FloatRotationComponent.class, RenderComponent.class);

    @Override
    public void update(final Engine engine) {
        for (var entity : engine.environment().fetchAll(FLOATINGS)) {
            final var wave = entity.get(FloatComponent.class).attachedWave;
            final var render = entity.get(RenderComponent.class);
            if (Objects.nonNull(wave)) {
                final double target = Rotation.of(wave).degrees() - 90;
                final double delta = target - render.options.rotation().degrees();
                final double change = delta * Math.min(1, engine.loop().delta() * entity.get(FloatRotationComponent.class).adjustmentSpeed);
                render.options = render.options.rotation(render.options.rotation().addDegrees(change));
            } else {
                render.options = render.options.rotation(Rotation.none());
            }
        }

    }

}
