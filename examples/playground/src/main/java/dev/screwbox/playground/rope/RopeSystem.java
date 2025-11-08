package dev.screwbox.playground.rope;

import dev.screwbox.core.Engine;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.playground.joint.JointComponent;

import static java.util.Objects.nonNull;

public class RopeSystem implements EntitySystem {

    private static final Archetype ROPES = Archetype.ofSpacial(RopeComponent.class, JointComponent.class);

    @Override
    public void update(final Engine engine) {
        for (final var rope : engine.environment().fetchAll(ROPES)) {
            final var config = rope.get(RopeComponent.class);
            if (config.nodes.isEmpty()) {
                addConnectedEntities(engine, rope, config);
            }
        }
    }

    private static void addConnectedEntities(final Engine engine, final Entity rope, final RopeComponent config) {
        JointComponent joint = rope.get(JointComponent.class);
        config.nodes.add(rope);
        while (nonNull(joint) && !joint.joints.isEmpty()) {
            final var targetId = joint.joints.getFirst().targetEntityId;
            final var targetEntity = engine.environment().fetchById(targetId);
            config.nodes.add(targetEntity);
            joint = targetEntity.get(JointComponent.class);
        }
    }
}
