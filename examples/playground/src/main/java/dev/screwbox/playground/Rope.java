package dev.screwbox.playground;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Line;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.core.TransformComponent;
import dev.screwbox.core.environment.importing.IdPool;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.core.environment.softphysics.RopeComponent;
import dev.screwbox.core.environment.softphysics.RopeRenderComponent;
import dev.screwbox.core.environment.softphysics.SoftLinkComponent;
import dev.screwbox.core.utils.Validate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class Rope {

    private final List<Entity> entities;

    private Rope(List<Entity> entities) {
        this.entities = entities;
    }

    public List<Entity> entities() {
        return entities;
    }
    public static Rope createRope(final Line rope, final int nodeCount, final IdPool pool) {
        List<Entity> entities = new ArrayList<>();
        Vector spacing = rope.start().substract(rope.end()).multiply(1.0 / nodeCount);
        int id = pool.allocateId();
        for (int i = nodeCount; i >= 0; i--) {
            boolean isStart = i == nodeCount;
            boolean isEnd = i == 0;

            Entity add = new Entity(id)
                .bounds(Bounds.atPosition(rope.end().add(spacing.multiply(i)), 1, 1));

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
        return new Rope(entities);
    }

    public Rope customizeRender(RopeRenderComponent ropeRenderComponent) {
        entities.getFirst().addOrReplace(ropeRenderComponent);
        return this;
    }

    public Rope friction(int friction) {
        Validate.zeroOrPositive(friction, "friction must be positive");
        entities.stream().map(e -> e.get(PhysicsComponent.class))
            .filter(Objects::nonNull)
            .forEach(physicsComponent -> physicsComponent.friction = friction);
        return this;
    }

    public Rope nodeSize(int size) {
        entities.forEach(e -> e.get(TransformComponent.class).bounds = Bounds.atPosition(e.position(), size, size));
        return this;
    }
}
