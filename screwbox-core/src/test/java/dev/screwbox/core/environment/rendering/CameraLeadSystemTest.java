package dev.screwbox.core.environment.rendering;

import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.internal.DefaultEnvironment;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.core.loop.Loop;
import dev.screwbox.core.test.EnvironmentExtension;
import org.assertj.core.data.Offset;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(EnvironmentExtension.class)
class CameraLeadSystemTest {

    @Test
    void update_updatesCameraTargetOffset(DefaultEnvironment environment, Loop loop) {
        when(loop.delta(5)).thenReturn(0.41);

        var camera = new CameraTargetComponent();

        environment
            .addEntity(new Entity()
                .add(new CameraLeadComponent())
                .add(camera)
                .add(new PhysicsComponent(), p -> p.velocity = Vector.of(30, 10)))

            .addSystem(new CameraLeadSystem());

        environment.update();

        assertThat(camera.offset.x()).isEqualTo(12.3, Offset.offset(0.01));
        assertThat(camera.offset.y()).isEqualTo(4.1, Offset.offset(0.01));
    }
}
