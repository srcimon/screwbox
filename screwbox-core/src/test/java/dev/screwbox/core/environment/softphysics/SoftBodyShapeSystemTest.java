package dev.screwbox.core.environment.softphysics;

import dev.screwbox.core.Polygon;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.core.TransformComponent;
import dev.screwbox.core.environment.internal.DefaultEnvironment;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.core.loop.Loop;
import dev.screwbox.core.test.EnvironmentExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;

import static dev.screwbox.core.Vector.$;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.offset;
import static org.mockito.Mockito.when;

@ExtendWith(EnvironmentExtension.class)
class SoftBodyShapeSystemTest {

    @Test
    void update_noOtherForceApplied_setsShapeAndDoesNotApplyForce(DefaultEnvironment environment, Loop loop) {
        when(loop.delta()).thenReturn(0.01);

        Entity firstNode = new Entity(2)
                .add(new SoftLinkComponent(3))
                .add(new PhysicsComponent())
                .add(new TransformComponent(4, 4, 0, 0));

        Entity secondNode = new Entity(3)
                .add(new TransformComponent(8, 8, 0, 0))
                .add(new PhysicsComponent())
                .add(new SoftLinkComponent(1));

        Entity start = new Entity(1)
                .add(new SoftBodyComponent())
                .add(new SoftBodyShapeComponent())
                .add(new PhysicsComponent())
                .add(new SoftLinkComponent(2))
                .add(new TransformComponent(2, 2, 0, 0));

        environment
                .addSystem(new SoftBodySystem())
                .addSystem(new SoftBodyShapeSystem())
                .addEntity(start)
                .addEntity(firstNode)
                .addEntity(secondNode);

        environment.updateTimes(4);

        Vector firstVelocity = firstNode.get(PhysicsComponent.class).velocity;
        Vector secondVelocity = secondNode.get(PhysicsComponent.class).velocity;
        Vector startVelocity = start.get(PhysicsComponent.class).velocity;
        assertThat(firstVelocity.length()).isZero();
        assertThat(secondVelocity.length()).isZero();
        assertThat(startVelocity.length()).isZero();
        assertThat(start.get(SoftBodyShapeComponent.class).shape).isEqualTo(Polygon.ofNodes(List.of($(2, 2), $(4, 4), $(8, 8), $(2, 2))));
    }

    @Test
    void update_entityMoves_appliesForce(DefaultEnvironment environment, Loop loop) {
        when(loop.delta()).thenReturn(0.01);

        Entity firstNode = new Entity(2)
                .add(new SoftLinkComponent(3))
                .add(new PhysicsComponent())
                .add(new TransformComponent(4, 4, 0, 0));

        Entity secondNode = new Entity(3)
                .add(new TransformComponent(8, 8, 0, 0))
                .add(new PhysicsComponent())
                .add(new SoftLinkComponent(1));

        Entity start = new Entity(1)
                .add(new SoftBodyComponent())
                .add(new SoftBodyShapeComponent())
                .add(new PhysicsComponent())
                .add(new SoftLinkComponent(2))
                .add(new TransformComponent(2, 2, 0, 0));

        environment
                .addSystem(new SoftBodySystem())
                .addSystem(new SoftBodyShapeSystem())
                .addEntity(start)
                .addEntity(firstNode)
                .addEntity(secondNode);
        environment.update();

        start.moveBy($(10, 4));

        environment.updateTimes(4);

        Vector firstVelocity = firstNode.get(PhysicsComponent.class).velocity;
        Vector secondVelocity = secondNode.get(PhysicsComponent.class).velocity;
        Vector startVelocity = start.get(PhysicsComponent.class).velocity;
        assertThat(firstVelocity.length()).isEqualTo(14.65, offset(0.01));
        assertThat(secondVelocity.length()).isZero();
        assertThat(startVelocity.length()).isEqualTo(40.92, offset(0.01));
        assertThat(start.get(SoftBodyShapeComponent.class).shape).isEqualTo(Polygon.ofNodes(List.of($(2, 2), $(4, 4), $(8, 8), $(2, 2))));
    }
}
