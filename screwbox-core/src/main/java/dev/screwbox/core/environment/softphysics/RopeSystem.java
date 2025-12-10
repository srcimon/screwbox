package dev.screwbox.core.environment.softphysics;

import dev.screwbox.core.Engine;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.Environment;
import dev.screwbox.core.environment.ExecutionOrder;

import java.util.List;

import static dev.screwbox.core.environment.Order.PREPARATION;
import static java.util.Objects.nonNull;

/**
 * Will update {@link RopeComponent#nodes}.
 *
 * @since 3.16.0
 */
@ExecutionOrder(PREPARATION)
public class RopeSystem implements EntitySystem {

    private static final Archetype ROPES = Archetype.ofSpacial(RopeComponent.class, SoftLinkComponent.class);

    @Override
    public void update(final Engine engine) {
        final var environment = engine.environment();
        for (final var rope : environment.fetchAll(ROPES)) {
            final var config = rope.get(RopeComponent.class);
            if (config.nodes.isEmpty()) {
                fillInRopeNodes(environment, rope, config.nodes);
            }
        }
    }

    private static void fillInRopeNodes(final Environment environment, final Entity start, final List<Entity> nodes) {
        var link = start.get(SoftLinkComponent.class);
        nodes.add(start);
        while (nonNull(link)) {
            final var targetEntity = environment.fetchById(link.targetId);
            if (start.equals(targetEntity)) {
                throw new IllegalArgumentException("rope starting from entity with id %s is looped".formatted(start.id().orElseThrow()));
            }
            nodes.add(targetEntity);
            link = targetEntity.get(SoftLinkComponent.class);
        }
    }
}
