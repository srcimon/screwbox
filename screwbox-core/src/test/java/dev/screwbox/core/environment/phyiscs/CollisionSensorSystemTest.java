package dev.screwbox.core.environment.phyiscs;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.core.TransformComponent;
import dev.screwbox.core.environment.internal.DefaultEnvironment;
import dev.screwbox.core.environment.physics.ColliderComponent;
import dev.screwbox.core.environment.physics.CollisionSensorComponent;
import dev.screwbox.core.environment.physics.CollisionSensorSystem;
import dev.screwbox.core.test.EnvironmentExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(EnvironmentExtension.class)
class CollisionSensorSystemTest {

    @Test
    void update_sensorHasInvalidRange(DefaultEnvironment environment) {

        environment.add(new Entity().bounds(Bounds.atPosition(1, 0, 2, 2))
                .add(new CollisionSensorComponent(), sensor -> sensor.range = -1))
                .addSystem(new CollisionSensorSystem());

       assertThatThrownBy(environment::update)
               .isInstanceOf(IllegalArgumentException.class)
               .hasMessage("sensor range must be positive (actual value: -1.0)");
    }

    @Test
    void update_collectsAllCollidedEntities(DefaultEnvironment environment) {
        Entity ball = new Entity().add(
                new TransformComponent(Bounds.atPosition(0, 0, 2, 2)),
                new ColliderComponent());

        Entity player = new Entity().add(
                new TransformComponent(Bounds.atPosition(1, 0, 2, 2)),
                new CollisionSensorComponent());

        environment.add(ball, player)
                .addSystem(new CollisionSensorSystem());

        environment.update();

        assertThat(player.get(CollisionSensorComponent.class).collidedEntities).contains(ball);
    }

    @Test
    void update_ignoresNonCollidedEntities(DefaultEnvironment environment) {
        Entity bird = new Entity().add(
                new TransformComponent(Bounds.atPosition(20, 10, 2, 2)),
                new ColliderComponent());

        Entity player = new Entity().add(
                new TransformComponent(Bounds.atPosition(1, 0, 2, 2)),
                new CollisionSensorComponent());

        environment.add(bird, player)
                .addSystem(new CollisionSensorSystem());

        environment.update();

        assertThat(player.get(CollisionSensorComponent.class).collidedEntities).isEmpty();
    }
}
