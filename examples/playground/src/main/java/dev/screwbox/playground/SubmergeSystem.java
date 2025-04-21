package dev.screwbox.playground;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Engine;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.Order;
import dev.screwbox.core.environment.physics.FloatComponent;
import dev.screwbox.core.environment.physics.PhysicsComponent;


public class SubmergeSystem implements EntitySystem {
    @Override
    public void update(Engine engine) {
        var physics = engine.environment().fetchAll(Archetype.ofSpacial(PhysicsComponent.class));
        for (var sinkable : engine.environment().fetchAll(Archetype.ofSpacial(SubmergeComponent.class, FloatComponent.class))) {
            FloatComponent floatComponent = sinkable.get(FloatComponent.class);
            SubmergeComponent sinkableComponent = sinkable.get(SubmergeComponent.class);
            floatComponent.buoyancy = Math.abs(floatComponent.buoyancy);
            Bounds testBounds = Bounds.atOrigin(sinkable.origin().add(1, -0.5), sinkable.bounds().width() - 2, 1);
            for (var p : physics) {
                if (sinkable != p && testBounds.touches(p.bounds())) {
                    sinkableComponent.lastContact = engine.loop().time();

                }
            }
            if (sinkableComponent.lastContact.addMillis(5).isAfter(engine.loop().time())) {
                floatComponent.buoyancy = -floatComponent.buoyancy;
            }
        }
    }
}
