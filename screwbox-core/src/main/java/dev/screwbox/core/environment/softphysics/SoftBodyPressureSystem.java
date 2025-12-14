package dev.screwbox.core.environment.softphysics;

import dev.screwbox.core.Engine;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.ExecutionOrder;
import dev.screwbox.core.environment.physics.PhysicsComponent;

import static dev.screwbox.core.environment.Order.SIMULATION_LATE;

@ExecutionOrder(SIMULATION_LATE)
public class SoftBodyPressureSystem implements EntitySystem {

    private static final Archetype BODIES = Archetype.ofSpacial(SoftBodyComponent.class, SoftLinkComponent.class, SoftBodyPressureComponent.class);

    @Override
    public void update(Engine engine) {
        for (final var body : engine.environment().fetchAll(BODIES)) {
            final var softBody = body.get(SoftBodyComponent.class);
            final var polygon = SoftPhysicsSupport.toPolygon(softBody);
            Vector sum = Vector.zero();
            final Vector[] appliedPressures = new Vector[polygon.nodes().size()];

            // calculate pressure according to position
            for (int i = 0; i < polygon.nodes().size(); i++) {
                final Entity entity = softBody.nodes.get(i);
                final var appliedPressure = polygon.center().substract(entity.position()).length(1)
                        .multiply(-engine.loop().delta() * body.get(SoftBodyPressureComponent.class).pressure);
                sum = sum.add(appliedPressure);
                appliedPressures[i] = appliedPressure;
            }

            // add pressure and rebalance velocity to avoid adding movement to the body
            for (int i = 0; i < polygon.nodes().size(); i++) {
                Entity entity = softBody.nodes.get(i);
                final var physics = entity.get(PhysicsComponent.class);
                physics.velocity = physics.velocity
                        .add(appliedPressures[i])
                        .add(sum.multiply(1.0 / polygon.nodes().size()).invert());
            }
        }
    }
}
