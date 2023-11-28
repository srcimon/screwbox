package io.github.srcimon.screwbox.examples.particles;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.entities.Archetype;
import io.github.srcimon.screwbox.core.entities.EntitySystem;
import io.github.srcimon.screwbox.core.entities.components.TransformComponent;
import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.World;

public class DrawEntityOutlineSystem implements EntitySystem {

    private static final Archetype TRANSFORMS = Archetype.of(TransformComponent.class);

    @Override
    public void update(Engine engine) {
        World world = engine.graphics().world();
        for (var entity : engine.entities().fetchAll(TRANSFORMS)) {
            var bounds = entity.get(TransformComponent.class).bounds;
            world.drawRectangle(bounds, Color.RED);
        }
    }
}
