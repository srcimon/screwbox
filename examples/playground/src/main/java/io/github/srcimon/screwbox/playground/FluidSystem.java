package io.github.srcimon.screwbox.playground;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.EntitySystem;

public class FluidSystem implements EntitySystem {

    private static final Archetype FLUIDS = Archetype.ofSpacial(FluidComponent.class);

    @Override
    public void update(Engine engine) {
        final double delta = engine.loop().delta();

        for (final var fluid : engine.environment().fetchAll(FLUIDS)) {
            fluid.get(FluidComponent.class).fluid.update(delta);
        }
    }
}
