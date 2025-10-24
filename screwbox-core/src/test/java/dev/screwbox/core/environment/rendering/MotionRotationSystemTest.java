package dev.screwbox.core.environment.rendering;

import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.internal.DefaultEnvironment;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.core.graphics.Sprite;
import dev.screwbox.core.loop.Loop;
import dev.screwbox.core.test.EnvironmentExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.offset;
import static org.mockito.Mockito.when;

@ExtendWith(EnvironmentExtension.class)
class MotionRotationSystemTest {

    @Test
    void update_rotationNonZero_updatesSpriteRotation(DefaultEnvironment environment, Loop loop) {
        when(loop.delta()).thenReturn(0.01);

        var renderComponent = new RenderComponent(Sprite.invisible());

        Entity body = new Entity().add(
                renderComponent,
                new MotionRotationComponent(),
                new PhysicsComponent(Vector.of(4, 4)));

        environment.addEntity(body)
                .addSystem(new MotionRotationSystem());

        environment.update();

        assertThat(renderComponent.options.rotation().degrees()).isEqualTo(67.5);

        environment.updateTimes(20);

        assertThat(renderComponent.options.rotation().degrees()).isEqualTo(135, offset(0.1));
    }

    @Test
    void update_rotationIsZero_doesntUpdateSpriteRotation(DefaultEnvironment environment) {
        Entity body = new Entity().add(
                new RenderComponent(Sprite.invisible()),
                new MotionRotationComponent(),
                new PhysicsComponent(Vector.zero()));

        environment.addEntity(body)
                .addSystem(new MotionRotationSystem());

        environment.update();

        var rotation = body.get(RenderComponent.class).options.rotation();
        assertThat(rotation.isZero()).isTrue();
    }
}
