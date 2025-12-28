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

import static dev.screwbox.core.Bounds.$$;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.offset;
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

        environment.importFromSource(ball, ground, gravity);

        environment.addSystems(
                new PhysicsSystem(),
                new GravitySystem(),
                new CollisionSensorSystem());

        environment.updateTimes(6);

        Vector ballPosition = ball.position();
        assertThat(ballPosition).isEqualTo(Vector.of(60, 190));
    }

    @Test
    void update_multipleUpdatesWithPhysicsThatUsesFriction_reducesSpeedToNada(DefaultEnvironment environment, Loop loop) {
        when(loop.delta()).thenReturn(0.4);

        environment.addEntity(new Entity()
                .bounds(Bounds.atOrigin(50, 0, 20, 20))
                .add(new PhysicsComponent(Vector.of(10, 4)), physics -> physics.friction = 4));

        environment.addSystem(new PhysicsSystem());

        environment.updateTimes(10);

        assertThat(environment.fetchSingletonComponent(PhysicsComponent.class).velocity).isEqualTo(Vector.zero());
    }

    @Test
    void update_physicsHasFriction_reducesSpeed(DefaultEnvironment environment, Loop loop) {
        when(loop.delta()).thenReturn(0.4);

        environment.addEntity(new Entity()
                .bounds(Bounds.atOrigin(50, 0, 20, 20))
                .add(new PhysicsComponent(Vector.of(10, 4)), physics -> physics.friction = 0.4));

        environment.addSystem(new PhysicsSystem());

        environment.update();

        Vector velocity = environment.fetchSingletonComponent(PhysicsComponent.class).velocity;
        assertThat(velocity.x()).isEqualTo(8.40, offset(0.01));
        assertThat(velocity.y()).isEqualTo(3.36, offset(0.01));
    }

    @Test
    void update_physicsWithMaxVelocityIsExposedToGravity_velocityIsLimitToMaximum(DefaultEnvironment environment, Loop loop) {
        when(loop.delta()).thenReturn(0.5);
        var physics = new PhysicsComponent();
        Entity body = new Entity()
                .bounds($$(10, 20, 4, 4))
                .add(physics, p -> p.maxVelocity = 40);

        environment
                .addEntity(body)
                .addEntity(new GravityComponent(Vector.y(100)))
                .addSystem(new GravitySystem())
                .addSystem(new PhysicsSystem());

        environment.updateTimes(200);

        assertThat(physics.velocity.length()).isEqualTo(40);
    }
}
