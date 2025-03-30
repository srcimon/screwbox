package io.github.srcimon.screwbox.playground;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Path;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.drawoptions.CircleDrawOptions;
import io.github.srcimon.screwbox.core.graphics.drawoptions.LineDrawOptions;

public class UpdateWaterSystem implements EntitySystem {

    private static final Archetype WATERS = Archetype.ofSpacial(WaterComponent.class);

    @Override
    public void update(Engine engine) {
        final double delta = engine.loop().delta();

        for (final var water : engine.environment().fetchAll(WATERS)) {
            water.get(WaterComponent.class).waterSurface.update(delta, water.bounds().width());
        }
    }
}
