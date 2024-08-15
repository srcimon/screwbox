package io.github.srcimon.screwbox.core.environment.rendering;

import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.internal.DefaultEnvironment;
import io.github.srcimon.screwbox.core.graphics.Sprite;
import io.github.srcimon.screwbox.core.loop.Loop;
import io.github.srcimon.screwbox.core.test.EnvironmentExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(EnvironmentExtension.class)
class FixedRotationSystemTest {

    @Test
    void update_rotationNonZero_updatesSpriteRotation(DefaultEnvironment environment, Loop loop) {
        when(loop.delta()).thenReturn(0.2);
        Entity body = new Entity().add(
                new RenderComponent(Sprite.invisible()),
                new FixedRotationComponent(2));

        environment.addEntity(body)
                .addSystem(new FixedRotationSystem());

        environment.update();

        var rotation = body.get(RenderComponent.class).options.rotation();
        assertThat(rotation.degrees()).isEqualTo(144.0);
    }

    @Test
    void update_rotationZero_spriteRotationStaysSame(DefaultEnvironment environment, Loop loop) {
        when(loop.delta()).thenReturn(0.2);
        Entity body = new Entity().add(
                new RenderComponent(Sprite.invisible()),
                new FixedRotationComponent(0));

        environment.addEntity(body)
                .addSystem(new FixedRotationSystem());

        environment.update();

        var rotation = body.get(RenderComponent.class).options.rotation();
        assertThat(rotation.degrees()).isZero();
    }
}
