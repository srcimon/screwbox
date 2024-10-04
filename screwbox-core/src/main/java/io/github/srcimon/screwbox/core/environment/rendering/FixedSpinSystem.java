package io.github.srcimon.screwbox.core.environment.rendering;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.graphics.Sprite;

/**
 * Spins {@link Sprite sprites} of {@link Entity entities} having {@link RenderComponent} and {@link FixedSpinComponent}.
 */
public class FixedSpinSystem implements EntitySystem {

    private static final Archetype SPINNING = Archetype.of(RenderComponent.class, FixedSpinComponent.class);

    @Override
    public void update(final Engine engine) {
        for (final var entity : engine.environment().fetchAll(SPINNING)) {
            final var render = entity.get(RenderComponent.class);
            final var fixedSpinComponent = entity.get(FixedSpinComponent.class);
            final double additionalRotation = engine.loop().delta(fixedSpinComponent.spinsPerSecond);
            final var spinValue = render.options.spin().addWithOverflow(additionalRotation);
            render.options = render.options.spin(spinValue).spinHorizontal(fixedSpinComponent.isSpinHorizontal);
        }
    }

}
