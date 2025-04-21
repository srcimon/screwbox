package dev.screwbox.playground;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Engine;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.physics.FloatComponent;
import dev.screwbox.core.environment.physics.PhysicsComponent;

public class SubmergeSystem implements EntitySystem {

    @Override
    public void update(Engine engine) {
        var physics = engine.environment().fetchAll(Archetype.ofSpacial(PhysicsComponent.class));
        for (var sinkable : engine.environment().fetchAll(Archetype.ofSpacial(SubmergeComponent.class, FloatComponent.class))) {
            FloatComponent floatComponent = sinkable.get(FloatComponent.class);
            SubmergeComponent submerge = sinkable.get(SubmergeComponent.class);
            Bounds testBounds = Bounds.atOrigin(sinkable.origin().add(1, -0.5), sinkable.bounds().width() - 2, 1);
            floatComponent.submerge = submerge.normal;
            for (var p : physics) {
                if (sinkable != p && testBounds.touches(p.bounds())) {
                    floatComponent.submerge = submerge.submerged;
                }
            }
        }
    }
}
