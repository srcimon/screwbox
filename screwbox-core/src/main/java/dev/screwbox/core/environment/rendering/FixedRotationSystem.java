package dev.screwbox.core.environment.rendering;

import dev.screwbox.core.Engine;
import dev.screwbox.core.Angle;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.graphics.Sprite;

/**
 * Rotates {@link Sprite sprites} of {@link Entity entities} having {@link RenderComponent} and {@link FixedRotationComponent}.
 */
public class FixedRotationSystem implements EntitySystem {

    private static final Archetype ROTATING = Archetype.of(RenderComponent.class, FixedRotationComponent.class);

    @Override
    public void update(Engine engine) {
        for (final var entity : engine.environment().fetchAll(ROTATING)) {
            final var render = entity.get(RenderComponent.class);
            final Angle additionalRotation = Angle.degrees(360 * engine.loop().delta() * entity.get(FixedRotationComponent.class).clockwiseRotationsPerSecond);
            final Angle rotation = render.options.rotation().add(additionalRotation);
            render.options = render.options.rotation(rotation);
        }
    }
}
