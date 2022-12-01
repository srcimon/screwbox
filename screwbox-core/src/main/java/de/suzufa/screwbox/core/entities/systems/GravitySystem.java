package de.suzufa.screwbox.core.entities.systems;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.Vector;
import de.suzufa.screwbox.core.entities.Archetype;
import de.suzufa.screwbox.core.entities.Entity;
import de.suzufa.screwbox.core.entities.EntitySystem;
import de.suzufa.screwbox.core.entities.Order;
import de.suzufa.screwbox.core.entities.SystemOrder;
import de.suzufa.screwbox.core.entities.components.GravityComponent;
import de.suzufa.screwbox.core.entities.components.PhysicsBodyComponent;

@Order(SystemOrder.SIMULATION_BEGIN)
public class GravitySystem implements EntitySystem {

    private static final Archetype GRAVITY_AFFECTED = Archetype.of(PhysicsBodyComponent.class);
    private static final Archetype GRAVITY = Archetype.of(GravityComponent.class);

    @Override
    public void update(Engine engine) {
        Entity gravityEntity = engine.entities().forcedFetch(GRAVITY);
        Vector gravity = gravityEntity.get(GravityComponent.class).gravity;
        Vector gravityDelta = gravity.multiply(engine.loop().delta());
        for (var entity : engine.entities().fetchAll(GRAVITY_AFFECTED)) {
            var physicsBodyComponent = entity.get(PhysicsBodyComponent.class);
            physicsBodyComponent.momentum = physicsBodyComponent.momentum
                    .add(gravityDelta.multiply(physicsBodyComponent.gravityModifier));
        }
    }
}