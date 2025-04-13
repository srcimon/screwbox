package dev.screwbox.core.environment.phyiscs;

import dev.screwbox.core.Time;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.core.TransformComponent;
import dev.screwbox.core.environment.internal.DefaultEnvironment;
import dev.screwbox.core.environment.physics.ColliderComponent;
import dev.screwbox.core.environment.physics.CollisionDetailsComponent;
import dev.screwbox.core.environment.physics.CollisionDetailsSystem;
import dev.screwbox.core.environment.physics.CollisionSensorComponent;
import dev.screwbox.core.environment.physics.CollisionSensorSystem;
import dev.screwbox.core.loop.Loop;
import dev.screwbox.core.test.EnvironmentExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static dev.screwbox.core.Bounds.$$;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(EnvironmentExtension.class)
class CollisionDetailsSystemTest {

    @Test
    void update_entityCollided_addsDetailsFromCollision(DefaultEnvironment environment, Loop loop) {
        when(loop.time()).thenReturn(Time.now());

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

        final var collisionDetails = player.get(CollisionDetailsComponent.class);
        assertThat(collisionDetails.entityBottom).isEqualTo(ball);
        assertThat(collisionDetails.lastBottomContact.isSet()).isTrue();
        assertThat(collisionDetails.touchesBottom).isTrue();
        assertThat(collisionDetails.touchesLeft).isFalse();
        assertThat(collisionDetails.touchesRight).isFalse();
        assertThat(collisionDetails.touchesTop).isFalse();
        assertThat(collisionDetails.entityLeft).isNull();
        assertThat(collisionDetails.entityRight).isNull();
        assertThat(collisionDetails.entityTop).isNull();
    }

    @Test
    void update_noCollidedEntity_resetsDetails(DefaultEnvironment environment, Loop loop) {
        when(loop.time()).thenReturn(Time.now());

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

        final var collisionDetails = player.get(CollisionDetailsComponent.class);
        assertThat(collisionDetails.touchesBottom).isFalse();
        assertThat(collisionDetails.touchesLeft).isFalse();
        assertThat(collisionDetails.touchesRight).isFalse();
        assertThat(collisionDetails.touchesTop).isFalse();
        assertThat(collisionDetails.entityBottom).isNull();
        assertThat(collisionDetails.entityLeft).isNull();
        assertThat(collisionDetails.entityRight).isNull();
        assertThat(collisionDetails.entityTop).isNull();
    }

}
