package dev.screwbox.core.environment.softphysics;

import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.core.TransformComponent;
import dev.screwbox.core.environment.internal.DefaultEnvironment;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.core.loop.Loop;
import dev.screwbox.core.test.EnvironmentExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.offset;
import static org.mockito.Mockito.when;

@ExtendWith(EnvironmentExtension.class)
class SoftBodyPressureSystemTest {

    @Test
    void update_addsOutgoingVelocityToAllNodesButSumIsZero(DefaultEnvironment environment, Loop loop) {
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
                .add(new SoftBodyPressureComponent(100))
                .add(new PhysicsComponent())
                .add(new SoftLinkComponent(2))
                .add(new TransformComponent(2, 2, 0, 0));

        environment
                .addSystem(new SoftBodySystem())
                .addSystem(new SoftBodyPressureSystem())
                .addEntity(start)
                .addEntity(firstNode)
                .addEntity(secondNode);

        environment.updateTimes(30);

        Vector firstVelocity = firstNode.get(PhysicsComponent.class).velocity;
        Vector secondVelocity = secondNode.get(PhysicsComponent.class).velocity;
        Vector startVelocity = start.get(PhysicsComponent.class).velocity;
        var sum = firstVelocity.add(secondVelocity).add(startVelocity);
        assertThat(sum.length()).isEqualTo(0.0, offset(0.01));
        assertThat(firstVelocity.length()).isEqualTo(20.32, offset(0.01));
        assertThat(secondVelocity.length()).isEqualTo(40.65, offset(0.01));
        assertThat(startVelocity.length()).isEqualTo(20.32, offset(0.01));
    }
}
