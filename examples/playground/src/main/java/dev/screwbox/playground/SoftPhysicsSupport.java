package dev.screwbox.playground;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.importing.IdPool;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.core.environment.softphysics.RopeComponent;
import dev.screwbox.core.environment.softphysics.SoftLinkComponent;

import java.util.ArrayList;
import java.util.List;

public final class SoftPhysicsSupport {

    private SoftPhysicsSupport() {
    }

    public static List<Entity> createRope(final Vector start, final Vector end, final int nodeCount, final IdPool pool) {
        List<Entity> entities = new ArrayList<>();
        Vector spacing = start.substract(end).multiply(1.0 / nodeCount);
        int id = pool.allocateId();
        for (int i = nodeCount; i >= 0; i--) {
            boolean isStart = i == nodeCount;
            boolean isEnd = i == 0;

            Entity add = new Entity(id)
                .bounds(Bounds.atPosition(end.add(spacing.multiply(i)), 1, 1));

            add.add(new PhysicsComponent());

            if (isStart) {
                add.add(new RopeComponent());
            }
            if (!isEnd) {
                add.add(new SoftLinkComponent(pool.peekId()));
            }
            entities.add(add);
            id = pool.allocateId();
        }
        return entities;
    }
}
