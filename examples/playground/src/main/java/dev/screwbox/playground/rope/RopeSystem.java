package dev.screwbox.playground.rope;

import dev.screwbox.core.Engine;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.Environment;
import dev.screwbox.playground.joint.JointComponent;

import java.util.List;

import static java.util.Objects.nonNull;

public class RopeSystem implements EntitySystem {

    private static final Archetype ROPES = Archetype.ofSpacial(RopeComponent.class, JointComponent.class);

    @Override
    public void update(final Engine engine) {
        final var environment = engine.environment();
        for (final var rope : environment.fetchAll(ROPES)) {
            final var config = rope.get(RopeComponent.class);
            if (config.nodes.isEmpty()) {
                extracted(environment, rope, config.nodes);
            }
        }
    }

    private static void extracted(final Environment environment, final Entity rope, final List<Entity> nodes) {
        var joint = rope.get(JointComponent.class);
        nodes.add(rope);
        while (nonNull(joint) && !joint.joints.isEmpty()) {
            final var targetId = joint.joints.getFirst().targetEntityId;

            final var targetEntity = environment.fetchById(targetId);
            nodes.add(targetEntity);
            joint = targetEntity.get(JointComponent.class);
        }
    }

}
