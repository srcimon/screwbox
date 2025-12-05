package dev.screwbox.core.environment.softphysics;

import dev.screwbox.core.Engine;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.Environment;
import dev.screwbox.core.environment.ExecutionOrder;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static dev.screwbox.core.environment.Order.SIMULATION_EARLY;
import static java.util.Objects.nonNull;
//TODO document SoftBodies in guide

/**
 * Will update {@link SoftBodyComponent#nodes}.
 *
 * @since 3.16.0
 */
@ExecutionOrder(SIMULATION_EARLY)
public class SoftBodySystem implements EntitySystem {
    private static final Archetype BODIES = Archetype.ofSpacial(SoftBodyComponent.class, SoftLinkComponent.class);

    @Override
    public void update(final Engine engine) {
        final var environment = engine.environment();
        for (final var body : environment.fetchAll(BODIES)) {
            final var config = body.get(SoftBodyComponent.class);
            if (config.nodes.isEmpty()) {
                extracted(environment, body, config.nodes);
            }
        }
    }

    private static void extracted(final Environment environment, final Entity softbody, final List<Entity> nodes) {
        Set<Entity> closed = new HashSet<>();
        var joint = softbody.get(SoftLinkComponent.class);
        boolean done = false;
        nodes.add(softbody);
        while (nonNull(joint) && !done) {

            final var targetEntity = environment.fetchById(joint.targetId);
            if (!closed.contains(targetEntity)) {
                nodes.add(targetEntity);
                joint = targetEntity.get(SoftLinkComponent.class);
                closed.add(targetEntity);
            } else {
                done = true;
            }
        }
    }
}
