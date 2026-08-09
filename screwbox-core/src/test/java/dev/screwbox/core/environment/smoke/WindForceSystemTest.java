package dev.screwbox.core.environment.smoke;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.internal.DefaultEnvironment;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.core.graphics.smoke.Smoke;
import dev.screwbox.core.loop.Loop;
import dev.screwbox.core.test.EnvironmentExtension;
import org.assertj.core.data.Offset;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static dev.screwbox.core.Vector.$;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(EnvironmentExtension.class)
class WindForceSystemTest {

    @Test
    void update_movesPhysicsEntitiesWithWind(DefaultEnvironment environment, Smoke smoke, Loop loop) {
        when(loop.delta(100)).thenReturn(0.1);
        when(smoke.velocityAt($(4, 1))).thenReturn(Vector.of(20, 30));
        PhysicsComponent physics = new PhysicsComponent();

        environment
            .addSystem(new WindForceSystem())
            .addEntity(new Entity()
                .bounds(Bounds.atPosition(4, 1, 10, 10))
                .add(physics)
                .add(new WindForceComponent()));

        environment.update();

        assertThat(physics.velocity.x()).isEqualTo(0.06, Offset.offset(0.01));
        assertThat(physics.velocity.y()).isEqualTo(0.08, Offset.offset(0.01));
    }
}
