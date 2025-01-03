package io.github.srcimon.screwbox.core.environment.phyiscs;

import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;
import io.github.srcimon.screwbox.core.environment.internal.DefaultEnvironment;
import io.github.srcimon.screwbox.core.environment.physics.ColliderComponent;
import io.github.srcimon.screwbox.core.environment.physics.CollisionDetailsComponent;
import io.github.srcimon.screwbox.core.environment.physics.CollisionDetailsSystem;
import io.github.srcimon.screwbox.core.environment.physics.CollisionSensorComponent;
import io.github.srcimon.screwbox.core.environment.physics.CollisionSensorSystem;
import io.github.srcimon.screwbox.core.test.EnvironmentExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static io.github.srcimon.screwbox.core.Bounds.$$;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(EnvironmentExtension.class)
class CollisionDetailsSystemTest {

    @Test
    void update_entityCollided_addsDetailsFromCollision(DefaultEnvironment environment) {
        Entity ball = new Entity().add(
                new TransformComponent($$(0, 16, 16, 16)),
                new ColliderComponent());

        Entity player = new Entity().add(
                new TransformComponent($$(0, 0, 16, 16)),
                new CollisionDetailsComponent(),
                new CollisionSensorComponent());

        environment.addEntities(ball, player)
                .addSystem(new CollisionSensorSystem())
                .addSystem(new CollisionDetailsSystem());

        environment.update();

        assertThat(player.get(CollisionDetailsComponent.class).entityBottom).isEqualTo(ball);
        assertThat(player.get(CollisionDetailsComponent.class).touchesBottom).isTrue();
        assertThat(player.get(CollisionDetailsComponent.class).touchesLeft).isFalse();
        assertThat(player.get(CollisionDetailsComponent.class).touchesRight).isFalse();
        assertThat(player.get(CollisionDetailsComponent.class).touchesTop).isFalse();
        assertThat(player.get(CollisionDetailsComponent.class).entityLeft).isNull();
        assertThat(player.get(CollisionDetailsComponent.class).entityRight).isNull();
        assertThat(player.get(CollisionDetailsComponent.class).entityTop).isNull();
    }

    @Test
    void update_noCollidedEntity_resetsDetails(DefaultEnvironment environment) {
        Entity ball = new Entity().add(
                new TransformComponent($$(0, 16, 16, 16)),
                new ColliderComponent());

        Entity player = new Entity().add(
                new TransformComponent($$(0, 0, 16, 16)),
                new CollisionDetailsComponent(),
                new CollisionSensorComponent());

        environment.addEntities(ball, player)
                .addSystem(new CollisionSensorSystem())
                .addSystem(new CollisionDetailsSystem());

        environment.update();
        environment.remove(ball);
        environment.update();

        assertThat(player.get(CollisionDetailsComponent.class).touchesBottom).isFalse();
        assertThat(player.get(CollisionDetailsComponent.class).touchesLeft).isFalse();
        assertThat(player.get(CollisionDetailsComponent.class).touchesRight).isFalse();
        assertThat(player.get(CollisionDetailsComponent.class).touchesTop).isFalse();
        assertThat(player.get(CollisionDetailsComponent.class).entityBottom).isNull();
        assertThat(player.get(CollisionDetailsComponent.class).entityLeft).isNull();
        assertThat(player.get(CollisionDetailsComponent.class).entityRight).isNull();
        assertThat(player.get(CollisionDetailsComponent.class).entityTop).isNull();
    }

}
