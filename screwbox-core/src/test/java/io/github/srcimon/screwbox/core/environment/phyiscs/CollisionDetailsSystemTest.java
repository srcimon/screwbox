package io.github.srcimon.screwbox.core.environment.phyiscs;

import io.github.srcimon.screwbox.core.Time;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;
import io.github.srcimon.screwbox.core.environment.internal.DefaultEnvironment;
import io.github.srcimon.screwbox.core.environment.physics.ColliderComponent;
import io.github.srcimon.screwbox.core.environment.physics.CollisionDetailsComponent;
import io.github.srcimon.screwbox.core.environment.physics.CollisionDetailsSystem;
import io.github.srcimon.screwbox.core.environment.physics.CollisionSensorComponent;
import io.github.srcimon.screwbox.core.environment.physics.CollisionSensorSystem;
import io.github.srcimon.screwbox.core.loop.Loop;
import io.github.srcimon.screwbox.core.test.EnvironmentExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static io.github.srcimon.screwbox.core.Bounds.$$;
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
        assertThat(collisionDetails.lastBottomContact.isSet()).isFalse();
        assertThat(collisionDetails.touchesLeft).isFalse();
        assertThat(collisionDetails.touchesRight).isFalse();
        assertThat(collisionDetails.touchesTop).isFalse();
        assertThat(collisionDetails.entityBottom).isNull();
        assertThat(collisionDetails.entityLeft).isNull();
        assertThat(collisionDetails.entityRight).isNull();
        assertThat(collisionDetails.entityTop).isNull();
    }

}
