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
 * Will update {@link SoftBodyComponent#nodes}.
 *
 * @since 3.16.0
 */
@ExecutionOrder(PREPARATION)
public class SoftBodySystem implements EntitySystem {
    private static final Archetype BODIES = Archetype.ofSpacial(SoftBodyComponent.class, SoftLinkComponent.class);

    @Override
    public void update(final Engine engine) {
        final var environment = engine.environment();
        for (final var body : environment.fetchAll(BODIES)) {
            final var config = body.get(SoftBodyComponent.class);
            if (config.nodes.isEmpty()) {
                fillInSoftBodyNodes(environment, body, config.nodes);
            }
            config.shape = SoftPhysicsSupport.toPolygon(config);
        }
    }

    private static void fillInSoftBodyNodes(final Environment environment, final Entity start, final List<Entity> nodes) {
        var link = start.get(SoftLinkComponent.class);
        nodes.add(start);
        while (nonNull(link)) {
            final var targetEntity = environment.fetchById(link.targetId);
            nodes.add(targetEntity);
            if (start.equals(targetEntity)) {
                return;
            }
            link = targetEntity.get(SoftLinkComponent.class);
        }
        throw new IllegalStateException("soft body is not closed");
    }
}
