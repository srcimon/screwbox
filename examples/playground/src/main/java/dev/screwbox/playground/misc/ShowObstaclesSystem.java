package dev.screwbox.playground.misc;

import dev.screwbox.core.Engine;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.options.RectangleDrawOptions;
import dev.screwbox.core.environment.ai.BoidObstacleComponent;

public class ShowObstaclesSystem implements EntitySystem {

    private static final Archetype OBSTACLES = Archetype.ofSpacial(BoidObstacleComponent.class);

    @Override
    public void update(Engine engine) {
        for (var obstacle : engine.environment().fetchAll(OBSTACLES)) {
        //    engine.graphics().world().drawRectangle(obstacle.bounds(), RectangleDrawOptions.outline(Color.WHITE).strokeWidth(2));
        }
    }
}
