package io.github.srcimon.screwbox.core.environment.rendering;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Rotation;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.graphics.Sprite;

/**
 * Rotates {@link Sprite sprites} of {@link Entity entities} having {@link RenderComponent} and {@link FixedRotationComponent}.
 */
public class FixedRotationSystem implements EntitySystem {

    private static final Archetype ROTATING = Archetype.of(RenderComponent.class, FixedRotationComponent.class);

    @Override
    public void update(Engine engine) {
        for (final var entity : engine.environment().fetchAll(ROTATING)) {
            final var render = entity.get(RenderComponent.class);
            double additionalRotation = 360 * engine.loop().delta() * entity.get(FixedRotationComponent.class).clockwiseRotationsPerSecond;
            final double rotation = render.options.rotation().degrees() + additionalRotation;
            render.options = render.options.rotation(Rotation.degrees(rotation));
        }
    }
}
