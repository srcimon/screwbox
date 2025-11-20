package dev.screwbox.playground.softbody;

import dev.screwbox.core.Engine;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.Environment;
import dev.screwbox.playground.joint.SoftJointComponent;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.Objects.nonNull;

public class SoftbodySystem implements EntitySystem {
    private static final Archetype SOFTBODIES = Archetype.ofSpacial(SoftbodyComponent.class, SoftJointComponent.class);

    @Override
    public void update(final Engine engine) {
        final var environment = engine.environment();
        for (final var softbody : environment.fetchAll(SOFTBODIES)) {
            final var config = softbody.get(SoftbodyComponent.class);
            if (config.nodes.isEmpty()) {
                extracted(environment, softbody, config.nodes);
            }
        }
    }

    private static void extracted(final Environment environment, final Entity softbody, final List<Entity> nodes) {
        Set<Entity> closed = new HashSet<>();
        var joint = softbody.get(SoftJointComponent.class);
        boolean done = false;
        nodes.add(softbody);
        while (nonNull(joint) && !done) {
            final var targetId = joint.joint.targetEntityId;

            final var targetEntity = environment.fetchById(targetId);
            if (!closed.contains(targetEntity)) {
                nodes.add(targetEntity);
                joint = targetEntity.get(SoftJointComponent.class);
                closed.add(targetEntity);
            } else {
                done = true;
            }
        }
    }
}
