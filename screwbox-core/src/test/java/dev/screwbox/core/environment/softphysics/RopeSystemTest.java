package dev.screwbox.core.environment.softphysics;

import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.core.TransformComponent;
import dev.screwbox.core.environment.internal.DefaultEnvironment;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.core.test.EnvironmentExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.offset;

@ExtendWith(EnvironmentExtension.class)
class RopeSystemTest {

    @Test
    void update_ropePresent_initializesNodeList(DefaultEnvironment environment) {
        Entity start = new Entity(1).name("rope-start")
            .add(new RopeComponent())
            .add(new SoftLinkComponent(2))
            .add(new TransformComponent());

        Entity firstNode = new Entity(2)
            .add(new SoftLinkComponent(3))
            .add(new TransformComponent());

        Entity secondNode = new Entity(3)
            .add(new TransformComponent());

        environment
            .addSystem(new RopeSystem())
            .addEntity(start)
            .addEntity(firstNode)
            .addEntity(secondNode);

        environment.update();

        assertThat(start.get(RopeComponent.class).nodes).containsExactly(start, firstNode, secondNode);

        environment.update();
        assertThat(start.get(RopeComponent.class).nodes).containsExactly(start, firstNode, secondNode);
        assertThat(start.get(RopeComponent.class).shape.definitionNotes()).containsExactly(start.position(), firstNode.position(), secondNode.position());
    }

    @Test
    void update_fixedLengthAndNodeIsMissingPhysicsComponent_throwsException(DefaultEnvironment environment) {
        var rope = SoftPhysicsSupport.createRope(Vector.$(10, 20), Vector.$(400, 200), 40, environment);
        rope.root().get(RopeComponent.class).isLengthFixed = true;

        rope.end().remove(PhysicsComponent.class);
        environment.addEntities(rope);
        environment.addSystem(new RopeSystem());

        assertThatThrownBy(environment::update)
            .isInstanceOf(IllegalStateException.class)
            .hasMessage("fixed length ropes are only supported for connected nodes that have a PhysicsComponent");
    }

    @Test
    void updated_usesFixedLength_allNodesHaveCorrectDistanceFromEachOther(DefaultEnvironment environment) {
        var rope = SoftPhysicsSupport.createRope(Vector.$(0, 0), Vector.$(400, 0), 11, environment);
        for (final var node : rope) {
            node.moveTo(Vector.random(100, 200));
        }

        rope.root().get(RopeComponent.class).isLengthFixed = true;

        environment.addEntities(rope);
        environment.addSystem(new RopeSystem());

        environment.update();

        for (var segment : rope.root().get(RopeComponent.class).shape.segments()) {
            assertThat(segment.length()).isEqualTo(40.0, offset(0.01));
        }
    }

    @Test
    void update_ropeIsLooped_throwsException(DefaultEnvironment environment) {
        Entity start = new Entity(1).name("rope-start")
            .add(new RopeComponent())
            .add(new SoftLinkComponent(2))
            .add(new TransformComponent());

        Entity firstNode = new Entity(2)
            .add(new SoftLinkComponent(3))
            .add(new TransformComponent());

        Entity secondNode = new Entity(3)
            .add(new SoftLinkComponent(1))
            .add(new TransformComponent());

        environment
            .addSystem(new RopeSystem())
            .addEntity(start)
            .addEntity(firstNode)
            .addEntity(secondNode);

        assertThatThrownBy(environment::update)
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("rope starting from entity with id 1 is looped");
    }
}
