package io.github.srcimon.screwbox.examples.animation.systems;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.entities.Archetype;
import io.github.srcimon.screwbox.core.entities.EntitySystem;
import io.github.srcimon.screwbox.core.entities.components.TransformComponent;
import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.examples.animation.components.DotComponent;

public class RenderDotsSystem implements EntitySystem {

    private static final Archetype DOTS = Archetype.of(DotComponent.class, TransformComponent.class);

    @Override
    public void update(Engine engine) {
        Bounds visibleArea = engine.graphics().world().visibleArea();
        for (var dot : engine.entities().fetchAll(DOTS)) {
            final Vector position = dot.get(TransformComponent.class).bounds.position();
            if (visibleArea.contains(position)) {
                final int diameter = dot.get(DotComponent.class).diameter;
                engine.graphics().world().fillCircle(position, diameter, Color.WHITE);
            }
        }
    }
}
