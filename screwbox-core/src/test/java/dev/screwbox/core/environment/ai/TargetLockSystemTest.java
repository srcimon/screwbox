package dev.screwbox.core.environment.ai;

import dev.screwbox.core.Rotation;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.internal.DefaultEnvironment;
import dev.screwbox.core.environment.rendering.RenderComponent;
import dev.screwbox.core.loop.Loop;
import dev.screwbox.core.test.EnvironmentExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static dev.screwbox.core.Bounds.$$;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(EnvironmentExtension.class)
class TargetLockSystemTest {

    RenderComponent renderComponent;

    @BeforeEach
    void setUp(DefaultEnvironment environment, Loop loop) {
        when(loop.delta(0.1)).thenReturn(0.5);

        renderComponent = new RenderComponent();

        Entity watcher = new Entity()
                .name("watcher")
                .add(renderComponent)
                .add(new TargetLockComponent(1), targetLock -> targetLock.speed = 0.1)
                .bounds($$(100, 0, 16, 16));

        environment
                .addSystem(new TargetLockSystem())
                .addEntity(watcher);
    }

    @Test
    void updade_targetEntityPresent_rotatesTowardsTarget(DefaultEnvironment environment) {
        environment.addEntity(new Entity(1)
                .name("target")
                .bounds($$(0, 0, 16, 16)));

        environment.update();

        assertThat(renderComponent.options.rotation()).isEqualTo(Rotation.degrees(-45.0));

        environment.update();

        assertThat(renderComponent.options.rotation()).isEqualTo(Rotation.degrees(-67.5));

        environment.updateTimes(10);

        assertThat(renderComponent.options.rotation()).isEqualTo(Rotation.degrees(-90));
    }

    @Test
    void updade_targetEntityMissing_doesntChangeRotation(DefaultEnvironment environment) {
        environment.update();

        assertThat(renderComponent.options.rotation()).isEqualTo(Rotation.none());

    }
}
