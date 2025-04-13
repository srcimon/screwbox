package dev.screwbox.core.environment.physics;

import dev.screwbox.core.Engine;
import dev.screwbox.core.Rotation;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.rendering.RenderComponent;

import static java.util.Objects.nonNull;

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
            final double target = nonNull(wave) ? Rotation.of(wave).degrees() - 90 : 0;
            final double delta = target - render.options.rotation().degrees();
            final double change = delta * Math.min(1, engine.loop().delta() * entity.get(FloatRotationComponent.class).adjustmentSpeed);
            render.options = render.options.rotation(render.options.rotation().addDegrees(change));
        }

    }

}
