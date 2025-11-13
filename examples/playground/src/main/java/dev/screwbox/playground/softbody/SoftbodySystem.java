package dev.screwbox.playground.softbody;

import dev.screwbox.core.Engine;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.Environment;
import dev.screwbox.playground.joint.JointComponent;
import dev.screwbox.playground.rope.RopeComponent;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.Objects.nonNull;

public class SoftbodySystem implements EntitySystem {
    private static final Archetype SOFTBODIES = Archetype.ofSpacial(SoftbodyComponent.class, JointComponent.class);

    @Override
    public void update(final Engine engine) {
        Set<Entity> closed = new HashSet<>();
        final var environment = engine.environment();
        for (final var softbody : environment.fetchAll(SOFTBODIES)) {
            closed.add(softbody);
            final var config = softbody.get(SoftbodyComponent.class);
            if (config.nodes.isEmpty()) {
                if(!closed.contains(softbody)) {
                    extracted(environment, softbody, config.nodes);
                }
            }
        }
    }

    private static void extracted(final Environment environment, final Entity softbody, final List<Entity> nodes) {
        var joint = softbody.get(JointComponent.class);
        nodes.add(softbody);
        while (nonNull(joint) && !joint.joints.isEmpty()) {
            final var targetId = joint.joints.getFirst().targetEntityId;

            final var targetEntity = environment.fetchById(targetId);
            nodes.add(targetEntity);
            joint = targetEntity.get(JointComponent.class);
        }
    }
}
