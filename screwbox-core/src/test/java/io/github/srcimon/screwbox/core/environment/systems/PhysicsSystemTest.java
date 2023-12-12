package io.github.srcimon.screwbox.core.environment.systems;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.components.*;
import io.github.srcimon.screwbox.core.environment.internal.DefaultEnvironment;
import io.github.srcimon.screwbox.core.loop.Loop;
import io.github.srcimon.screwbox.core.test.EnvironmentExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(EnvironmentExtension.class)
class PhysicsSystemTest {

    @Test
    void update_updatesPositionOfPhysicItems(DefaultEnvironment entities, Loop loop) {
        when(loop.delta()).thenReturn(0.5);
        Entity body = new Entity().add(
                new TransformComponent(Bounds.atPosition(0, 0, 10, 10)),
                new PhysicsBodyComponent(Vector.of(4, 4)));

        entities.addEntity(body);
        entities.addSystem(new PhysicsSystem());

        entities.update();

        Vector center = body.get(TransformComponent.class).bounds.position();
        assertThat(center).isEqualTo(Vector.of(2, 2));
    }

    @Test
    void update_physicBodiesCollideWithEnvironment(DefaultEnvironment entities, Loop loop) {
        when(loop.delta()).thenReturn(0.8);

        Entity ball = new Entity().add(
                new TransformComponent(Bounds.atOrigin(50, 0, 20, 20)),
                new PhysicsBodyComponent(),
                new CollisionSensorComponent());

        Entity ground = new Entity().add(
                new TransformComponent(Bounds.atOrigin(0, 200, 140, 40)),
                new ColliderComponent());

        Entity gravity = new Entity().add(new GravityComponent(Vector.of(0, 20)));

        entities.addSystem(ball, ground, gravity);

        entities.addSystems(
                new PhysicsSystem(),
                new GravitySystem(),
                new CollisionSensorSystem());

        entities.updateTimes(6);

        Vector ballPosition = ball.get(TransformComponent.class).bounds.position();
        assertThat(ballPosition).isEqualTo(Vector.of(60, 190));
    }

}
