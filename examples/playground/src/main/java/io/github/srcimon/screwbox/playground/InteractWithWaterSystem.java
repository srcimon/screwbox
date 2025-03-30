package io.github.srcimon.screwbox.playground;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Path;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.EntitySystem;

public class InteractWithWaterSystem implements EntitySystem {

    private static final Archetype WATERS = Archetype.ofSpacial(WaterComponent.class);

    @Override
    public void update(Engine engine) {
        if (engine.mouse().isPressedLeft()) {
            for (final var water : engine.environment().fetchAll(WATERS)) {

                WaterSurface waterSurface = water.get(WaterComponent.class).waterSurface;
                Path surfacePath = waterSurface.surface(water.origin(), water.bounds().width());


                int nr = 0;
                int minNr = 0;
                double minDistance = Double.MAX_VALUE;

                for (var node : surfacePath.nodes()) {
                    double distance = node.distanceTo(engine.mouse().position());
                    if (distance < minDistance) {
                        minDistance = distance;
                        minNr = nr;
                    }
                    nr++;
                }


                waterSurface.interact(minNr, 100);
            }
        }
    }
}
