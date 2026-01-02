package dev.screwbox.core.environment;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Line;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.importing.IdPool;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.core.environment.softphysics.RopeComponent;
import dev.screwbox.core.environment.softphysics.SoftLinkComponent;

import java.util.ArrayList;
import java.util.List;

public final class SoftPhysicsSupport {

    private SoftPhysicsSupport() {
    }

    public static List<Entity> createRope(Line rope, int nodeCount, IdPool pool) {
        List<Entity> entities = new ArrayList<>();
        Vector spacing = rope.start().substract(rope.end()).multiply(1.0 / nodeCount);
        int id = pool.allocateId();
        for (int i = nodeCount; i >= 0; i--) {
            Entity add = new Entity(id)
                .bounds(Bounds.atPosition(rope.end().add(spacing.multiply(i)), 4, 4))
                .add(new PhysicsComponent());

            if (i == nodeCount) {
                add.add(new RopeComponent());
            }
            if (i != 0) {
                add.add(new SoftLinkComponent(pool.peekId()));
            }
            entities.add(add);
            id = pool.allocateId();
        }
        return entities;
    }
}
