package io.github.srcimon.screwbox.core.environment.rendering;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.Rotation;
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
            final double spin = calculateSpin(render, additionalRotation);
            render.options = render.options.spin(Percent.of(spin)).spinHorizontal(fixedSpinComponent.isSpinHorizontal);
        }
    }

    private static double calculateSpin(RenderComponent render, double additionalRotation) {
        final var spin =  render.options.spin().value() + additionalRotation;
        if(spin < 0) {
            return spin + 1;
        }
        if(spin > 1) {
            return spin - 1;
        }
        return spin;
    }

}
