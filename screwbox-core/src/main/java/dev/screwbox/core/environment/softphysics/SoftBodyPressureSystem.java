package dev.screwbox.core.environment.softphysics;

import dev.screwbox.core.Engine;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.ExecutionOrder;
import dev.screwbox.core.environment.physics.PhysicsComponent;

import static dev.screwbox.core.environment.Order.SIMULATION_LATE;
import static java.util.Objects.nonNull;

@ExecutionOrder(SIMULATION_LATE)
public class SoftBodyPressureSystem implements EntitySystem {

    private static final Archetype BODIES = Archetype.ofSpacial(SoftBodyComponent.class, SoftLinkComponent.class, PhysicsComponent.class, SoftBodyPressureComponent.class);

    @Override
    public void update(Engine engine) {
        for (final var body : engine.environment().fetchAll(BODIES)) {
            final var softBody = body.get(SoftBodyComponent.class);
            if (nonNull(softBody.shape)) {
                final var config = body.get(SoftBodyPressureComponent.class);
                final Vector[] appliedPressures = new Vector[softBody.shape.nodes().size()];

                // calculate pressure according to position relative to center
                for (int i = 0; i < softBody.shape.nodes().size(); i++) {
                    final Entity entity = softBody.nodes.get(i);
                    final var appliedPressure = softBody.shape.center().substract(entity.position()).length(1)
                            .multiply(-engine.loop().delta() * config.pressure);
                    appliedPressures[i] = appliedPressure;
                }

                final Vector rebalanceVelocity = calculateRebalanceVelocity(appliedPressures);

                // add pressure and rebalance velocity to avoid adding movement to the body
                for (int i = 0; i < softBody.shape.nodes().size(); i++) {
                    Entity entity = softBody.nodes.get(i);
                    final var physics = entity.get(PhysicsComponent.class);
                    physics.velocity = physics.velocity
                            .add(appliedPressures[i])
                            .add(rebalanceVelocity);
                }

//                Vector all = Vector.zero();
//                for (int i = 0; i < softBody.shape.nodes().size(); i++) {
//                    all.add(appliedPressures[i]).add(rebalanceVelocity);
//                }
//                System.out.println(all);
            }
        }
    }

    private static Vector calculateRebalanceVelocity(final Vector[] appliedPressures) {
        Vector sum = Vector.zero();
        for (final Vector appliedPressure : appliedPressures) {
            sum = sum.add(appliedPressure);
        }
        return sum.multiply(1.0 / appliedPressures.length).invert();
    }
}
