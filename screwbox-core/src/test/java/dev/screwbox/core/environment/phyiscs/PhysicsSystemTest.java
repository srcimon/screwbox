package dev.screwbox.core.environment.phyiscs;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.core.TransformComponent;
import dev.screwbox.core.environment.internal.DefaultEnvironment;
import dev.screwbox.core.environment.physics.ColliderComponent;
import dev.screwbox.core.environment.physics.CollisionSensorComponent;
import dev.screwbox.core.environment.physics.CollisionSensorSystem;
import dev.screwbox.core.environment.physics.GravityComponent;
import dev.screwbox.core.environment.physics.GravitySystem;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.core.environment.physics.PhysicsSystem;
import dev.screwbox.core.loop.Loop;
import dev.screwbox.core.test.EnvironmentExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(EnvironmentExtension.class)
class PhysicsSystemTest {

    @Test
    void update_updatesPositionOfPhysicItems(DefaultEnvironment environment, Loop loop) {
        when(loop.delta()).thenReturn(0.5);
        Entity body = new Entity().add(
                new TransformComponent(Bounds.atPosition(0, 0, 10, 10)),
                new PhysicsComponent(Vector.of(4, 4)));

        environment.addEntity(body);
        environment.addSystem(new PhysicsSystem());

        environment.update();

        Vector center = body.position();
        assertThat(center).isEqualTo(Vector.of(2, 2));
    }

    @Test
    void update_physicBodiesCollideWithEnvironment(DefaultEnvironment environment, Loop loop) {
        when(loop.delta()).thenReturn(0.8);

        Entity ball = new Entity().add(
                new TransformComponent(Bounds.atOrigin(50, 0, 20, 20)),
                new PhysicsComponent(),
                new CollisionSensorComponent());

        Entity ground = new Entity().add(
                new TransformComponent(Bounds.atOrigin(0, 200, 140, 40)),
                new ColliderComponent());

        Entity gravity = new Entity().add(new GravityComponent(Vector.of(0, 20)));

        environment.addEntities(ball, ground, gravity);

        environment.addSystems(
                new PhysicsSystem(),
                new GravitySystem(),
                new CollisionSensorSystem());

        environment.updateTimes(6);

        Vector ballPosition = ball.position();
        assertThat(ballPosition).isEqualTo(Vector.of(60, 190));
    }

}
