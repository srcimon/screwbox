package io.github.srcimon.screwbox.playground;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.EntitySystem;

public class UpdateWaterSystem implements EntitySystem {

    private static final Archetype WATERS = Archetype.ofSpacial(FluidComponent.class);

    @Override
    public void update(Engine engine) {
        final double delta = engine.loop().delta();

        for (final var water : engine.environment().fetchAll(WATERS)) {
            water.get(FluidComponent.class).waterSurface.update(delta);
        }
    }
}
