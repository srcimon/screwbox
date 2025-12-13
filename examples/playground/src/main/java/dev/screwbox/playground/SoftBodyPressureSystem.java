package dev.screwbox.playground;

import dev.screwbox.core.Engine;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.ExecutionOrder;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.core.environment.softphysics.SoftBodyComponent;
import dev.screwbox.core.environment.softphysics.SoftLinkComponent;
import dev.screwbox.core.environment.softphysics.SoftPhysicsSupport;
import dev.screwbox.core.keyboard.Key;

import static dev.screwbox.core.environment.Order.SIMULATION_LATE;

@ExecutionOrder(SIMULATION_LATE)
public class SoftBodyPressureSystem implements EntitySystem {

    private static final Archetype BODIES = Archetype.ofSpacial(SoftBodyComponent.class, SoftLinkComponent.class, SoftBodyPressureComponent.class);

    @Override
    public void update(Engine engine) {

        for (final var body : engine.environment().fetchAll(BODIES)) {
            if (engine.keyboard().isDown(Key.T)) {
                body.get(SoftBodyPressureComponent.class).pressure -= 200;
            }
            if (engine.keyboard().isDown(Key.Z)) {
                body.get(SoftBodyPressureComponent.class).pressure += 200;
            }
            final var softBody = body.get(SoftBodyComponent.class);
            final var polygon = SoftPhysicsSupport.toPolygon(softBody);
            Vector sum = Vector.zero();
            for (int i = 0; i < polygon.nodes().size(); i++) {
                Entity entity = softBody.nodes.get(i);
                var delta = polygon.center().substract(entity.position()).length(1).multiply(-engine.loop().delta() * body.get(SoftBodyPressureComponent.class).pressure);
                sum = sum.add(delta);
                entity.get(PhysicsComponent.class).velocity = entity.get(PhysicsComponent.class).velocity.add(delta);
            }
            for (int i = 0; i < polygon.nodes().size(); i++) {
                Entity entity = softBody.nodes.get(i);
                entity.get(PhysicsComponent.class).velocity = entity.get(PhysicsComponent.class).velocity.add(sum.multiply(1.0 / polygon.nodes().size()).invert());
            }
        }
    }
}
