package dev.screwbox.core.environment.rendering;

import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.internal.DefaultEnvironment;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.core.graphics.Sprite;
import dev.screwbox.core.test.EnvironmentExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(EnvironmentExtension.class)
class MovementAngleSystemTest {

    @Test
    void update_rotationNonZero_updatesSpriteRotation(DefaultEnvironment environment) {
        Entity body = new Entity().add(
                new RenderComponent(Sprite.invisible()),
                new MovementRotationComponent(),
                new PhysicsComponent(Vector.of(4, 4)));

        environment.addEntity(body)
                .addSystem(new MovementRotationSystem());

        environment.update();

        var rotation = body.get(RenderComponent.class).options.rotation();
        assertThat(rotation.degrees()).isEqualTo(135);
    }

    @Test
    void update_rotationIsZero_doesntUpdateSpriteRotation(DefaultEnvironment environment) {
        Entity body = new Entity().add(
                new RenderComponent(Sprite.invisible()),
                new MovementRotationComponent(),
                new PhysicsComponent(Vector.zero()));

        environment.addEntity(body)
                .addSystem(new MovementRotationSystem());

        environment.update();

        var rotation = body.get(RenderComponent.class).options.rotation();
        assertThat(rotation.isNone()).isTrue();
    }
}
