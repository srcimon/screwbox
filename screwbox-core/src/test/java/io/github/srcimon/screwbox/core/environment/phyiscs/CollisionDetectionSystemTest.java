package io.github.srcimon.screwbox.core.environment.phyiscs;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.physics.ColliderComponent;
import io.github.srcimon.screwbox.core.environment.physics.CollisionDetectionComponent;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;
import io.github.srcimon.screwbox.core.environment.internal.DefaultEnvironment;
import io.github.srcimon.screwbox.core.environment.physics.CollisionDetectionSystem;
import io.github.srcimon.screwbox.core.test.EnvironmentExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(EnvironmentExtension.class)
class CollisionDetectionSystemTest {

    @Test
    void update_informsCollidedEntities(DefaultEnvironment environment) {
        Entity ball = new Entity().add(
                new TransformComponent(Bounds.atPosition(0, 0, 2, 2)),
                new ColliderComponent());

        Entity player = new Entity().add(
                new TransformComponent(Bounds.atPosition(1, 0, 2, 2)),
                new CollisionDetectionComponent());

        environment.addEntities(ball, player)
                .addSystem(new CollisionDetectionSystem());

        environment.update();

        var collidedEntities = player.get(CollisionDetectionComponent.class).collidedEntities;
        assertThat(collidedEntities).contains(ball);
    }

    @Test
    void update_ignoresNonCollidedEntities(DefaultEnvironment environment) {
        Entity bird = new Entity().add(
                new TransformComponent(Bounds.atPosition(20, 10, 2, 2)),
                new ColliderComponent());

        Entity player = new Entity().add(
                new TransformComponent(Bounds.atPosition(1, 0, 2, 2)),
                new CollisionDetectionComponent());

        environment.addEntities(bird, player)
                .addSystem(new CollisionDetectionSystem());

        environment.update();

        var collidedEntities = player.get(CollisionDetectionComponent.class).collidedEntities;
        assertThat(collidedEntities).isEmpty();
    }
}
