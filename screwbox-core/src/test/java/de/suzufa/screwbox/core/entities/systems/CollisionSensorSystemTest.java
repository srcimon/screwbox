package de.suzufa.screwbox.core.entities.systems;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import de.suzufa.screwbox.core.Bounds;
import de.suzufa.screwbox.core.entities.Entity;
import de.suzufa.screwbox.core.entities.components.ColliderComponent;
import de.suzufa.screwbox.core.entities.components.CollisionSensorComponent;
import de.suzufa.screwbox.core.entities.components.TransformComponent;
import de.suzufa.screwbox.core.entities.internal.DefaultEntities;
import de.suzufa.screwbox.core.test.EntityEngineExtension;

@ExtendWith(EntityEngineExtension.class)
class CollisionSensorSystemTest {

    @Test
    void update_informsCollidedEntities(DefaultEntities entityEngine) {
        Entity ball = new Entity().add(
                new TransformComponent(Bounds.atPosition(0, 0, 2, 2)),
                new ColliderComponent());

        Entity player = new Entity().add(
                new TransformComponent(Bounds.atPosition(1, 0, 2, 2)),
                new CollisionSensorComponent());

        entityEngine.add(ball, player)
                .add(new CollisionSensorSystem());

        entityEngine.update();

        var collidedEntities = player.get(CollisionSensorComponent.class).collidedEntities;
        assertThat(collidedEntities).contains(ball);
    }

    @Test
    void update_ignoresNonCollidedEntities(DefaultEntities entityEngine) {
        Entity bird = new Entity().add(
                new TransformComponent(Bounds.atPosition(20, 10, 2, 2)),
                new ColliderComponent());

        Entity player = new Entity().add(
                new TransformComponent(Bounds.atPosition(1, 0, 2, 2)),
                new CollisionSensorComponent());

        entityEngine.add(bird, player)
                .add(new CollisionSensorSystem());

        entityEngine.update();

        var collidedEntities = player.get(CollisionSensorComponent.class).collidedEntities;
        assertThat(collidedEntities).isEmpty();
    }
}
