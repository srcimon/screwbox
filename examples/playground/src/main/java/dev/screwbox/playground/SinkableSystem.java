package dev.screwbox.playground;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Engine;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.physics.FloatComponent;
import dev.screwbox.core.environment.physics.PhysicsComponent;

public class SinkableSystem implements EntitySystem {
    @Override
    public void update(Engine engine) {
        var physics = engine.environment().fetchAll(Archetype.ofSpacial(PhysicsComponent.class));
        for (var sinkable : engine.environment().fetchAll(Archetype.ofSpacial(SinkableComponent.class, FloatComponent.class))) {
            sinkable.get(FloatComponent.class).buoyancy = 400;
            Bounds testBounds = Bounds.atOrigin(sinkable.origin().add(1, -0.5), sinkable.bounds().width()-2, 1);
            for (var p : physics) {
                if (sinkable != p && testBounds.touches(p.bounds())) {//TODO top only
                    sinkable.get(FloatComponent.class).buoyancy = -400;
                }
            }
        }
    }
}
