package dev.screwbox.core.environment.softphysics;

import dev.screwbox.core.Engine;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.Environment;
import dev.screwbox.core.environment.ExecutionOrder;

import java.util.List;

import static dev.screwbox.core.environment.Order.SIMULATION_EARLY;
import static java.util.Objects.nonNull;

@ExecutionOrder(SIMULATION_EARLY)
public class RopeSystem implements EntitySystem {

    private static final Archetype ROPES = Archetype.ofSpacial(RopeComponent.class, SoftLinkComponent.class);

    @Override
    public void update(final Engine engine) {
        final var environment = engine.environment();
        for (final var rope : environment.fetchAll(ROPES)) {
            final var config = rope.get(RopeComponent.class);
            if (config.nodes.isEmpty()) {
                fillInRope(environment, rope, config.nodes);
            }
        }
    }

    //TODO prevent infinite loop

    private static void fillInRope(final Environment environment, final Entity start, final List<Entity> nodes) {
        var joint = start.get(SoftLinkComponent.class);
        nodes.add(start);
        while (nonNull(joint)) {
            final var targetEntity = environment.fetchById(joint.targetId);
            nodes.add(targetEntity);
            joint = targetEntity.get(SoftLinkComponent.class);
        }
    }
}
