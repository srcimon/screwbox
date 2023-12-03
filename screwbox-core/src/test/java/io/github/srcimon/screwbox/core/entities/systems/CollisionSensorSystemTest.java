package io.github.srcimon.screwbox.core.entities.systems;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.entities.Entity;
import io.github.srcimon.screwbox.core.entities.components.ColliderComponent;
import io.github.srcimon.screwbox.core.entities.components.CollisionSensorComponent;
import io.github.srcimon.screwbox.core.entities.components.TransformComponent;
import io.github.srcimon.screwbox.core.entities.internal.DefaultEntities;
import io.github.srcimon.screwbox.core.test.EntitiesExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(EntitiesExtension.class)
class CollisionSensorSystemTest {

    @Test
    void update_informsCollidedEntities(DefaultEntities entities) {
        Entity ball = new Entity().add(
                new TransformComponent(Bounds.atPosition(0, 0, 2, 2)),
                new ColliderComponent());

        Entity player = new Entity().add(
                new TransformComponent(Bounds.atPosition(1, 0, 2, 2)),
                new CollisionSensorComponent());

        entities.addSystem(ball, player)
                .addSystem(new CollisionSensorSystem());

        entities.update();

        var collidedEntities = player.get(CollisionSensorComponent.class).collidedEntities;
        assertThat(collidedEntities).contains(ball);
    }

    @Test
    void update_ignoresNonCollidedEntities(DefaultEntities entities) {
        Entity bird = new Entity().add(
                new TransformComponent(Bounds.atPosition(20, 10, 2, 2)),
                new ColliderComponent());

        Entity player = new Entity().add(
                new TransformComponent(Bounds.atPosition(1, 0, 2, 2)),
                new CollisionSensorComponent());

        entities.addSystem(bird, player)
                .addSystem(new CollisionSensorSystem());

        entities.update();

        var collidedEntities = player.get(CollisionSensorComponent.class).collidedEntities;
        assertThat(collidedEntities).isEmpty();
    }
}
