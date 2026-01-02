package dev.screwbox.core.environment;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Line;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.importing.IdPool;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.core.environment.softphysics.RopeComponent;
import dev.screwbox.core.environment.softphysics.RopeRenderComponent;
import dev.screwbox.core.environment.softphysics.SoftLinkComponent;
import dev.screwbox.core.graphics.Size;
import dev.screwbox.core.utils.Validate;

import java.util.ArrayList;
import java.util.List;

public final class SoftPhysicsSupport {

    public record RopeOptions(int nodeCount, Size nodeSize, boolean startCanMove, boolean endCanMove, double friction) {

        public static RopeOptions nodeCount(int nodeCount) {
            return new RopeOptions(nodeCount, Size.square(1), true, true, 2);
        }

        public RopeOptions {
            Validate.range(nodeCount, 3, 1024, "nodeCount must be in range between 1 and 1024");
            Validate.zeroOrPositive(friction, "friction must be positive");
        }

        public RopeOptions fixedStart() {
            return new RopeOptions(nodeCount, nodeSize, false, endCanMove, friction);
        }

        public RopeOptions fixedEnd() {
            return new RopeOptions(nodeCount, nodeSize, startCanMove, false, friction);
        }

        public RopeOptions nodeSize(Size nodeSize) {
            return new RopeOptions(nodeCount, nodeSize, startCanMove, endCanMove, friction);
        }

        public RopeOptions friction(double friction) {
            return new RopeOptions(nodeCount, nodeSize, startCanMove, endCanMove, friction);
        }

        public RopeOptions renderComponent(RopeRenderComponent ropeRenderComponent) {
            return null;
        }
    }

    private SoftPhysicsSupport() {
    }

    public static List<Entity> createRope(final Line rope, final RopeOptions ropeOptions, final IdPool pool) {
        List<Entity> entities = new ArrayList<>();
        Vector spacing = rope.start().substract(rope.end()).multiply(1.0 / ropeOptions.nodeCount());
        int id = pool.allocateId();
        for (int i = ropeOptions.nodeCount(); i >= 0; i--) {
            boolean isStart = i == ropeOptions.nodeCount();
            boolean isEnd = i == 0;

            Entity add = new Entity(id)
                .bounds(Bounds.atPosition(rope.end().add(spacing.multiply(i)), ropeOptions.nodeSize().width(), ropeOptions.nodeSize().height()));

            if(ropeOptions.startCanMove() && isStart) {
                 add.add(new PhysicsComponent(), p -> p.friction = ropeOptions.friction());
            }
            if(ropeOptions.endCanMove() && isEnd) {
                add.add(new PhysicsComponent(), p -> p.friction = ropeOptions.friction());
            }
            if(!isEnd && !isStart) {
                add.add(new PhysicsComponent(), p -> p.friction = ropeOptions.friction());
            }

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
