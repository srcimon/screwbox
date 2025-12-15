package dev.screwbox.core.environment.softphysics;

import dev.screwbox.core.Engine;
import dev.screwbox.core.Polygon;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.Environment;
import dev.screwbox.core.environment.ExecutionOrder;

import java.util.ArrayList;
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
            config.shape = toPolygon(config);
        }
    }

    private static void fillInSoftBodyNodes(final Environment environment, final Entity start, final List<Entity> nodes) {
        var link = start.get(SoftLinkComponent.class);
        nodes.add(start);
        Entity lastTargetEntity = start;
        while (nonNull(link)) {
            final var targetEntity = environment.fetchById(link.targetId);
            if(targetEntity.equals(lastTargetEntity)) {
                throw new IllegalArgumentException("soft link of entity with id %s is linked to self".formatted(link.targetId));
            }
            nodes.add(targetEntity);
            if (start.equals(targetEntity)) {
                return;
            }
            link = targetEntity.get(SoftLinkComponent.class);
            lastTargetEntity = targetEntity;
        }
        throw new IllegalStateException("soft body is not closed");
    }

    private static Polygon toPolygon(final SoftBodyComponent softBody) {
        final List<Vector> nodes = new ArrayList<>();
        for (final var node : softBody.nodes) {
            nodes.add(node.position());
        }
        return Polygon.ofNodes(nodes);
    }
}
