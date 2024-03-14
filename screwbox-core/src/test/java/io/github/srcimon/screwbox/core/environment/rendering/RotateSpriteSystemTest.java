package io.github.srcimon.screwbox.core.environment.rendering;

import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.internal.DefaultEnvironment;
import io.github.srcimon.screwbox.core.environment.physics.PhysicsComponent;
import io.github.srcimon.screwbox.core.graphics.Sprite;
import io.github.srcimon.screwbox.core.test.EnvironmentExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(EnvironmentExtension.class)
class RotateSpriteSystemTest {

    @Test
    void update_rotationNonZero_updatesSpriteRotation(DefaultEnvironment environment) {
        Entity body = new Entity().add(
                new RenderComponent(Sprite.invisible()),
                new RotateSpriteComponent(),
                new PhysicsComponent(Vector.of(4, 4)));

        environment.addEntity(body)
                .addSystem(new RotateSpriteSystem());

        environment.update();

        var rotation = body.get(RenderComponent.class).options.rotation();
        assertThat(rotation.degrees()).isEqualTo(135);
    }

    @Test
    void update_rotationIsZero_doesntUpdateSpriteRotation(DefaultEnvironment environment) {
        Entity body = new Entity().add(
                new RenderComponent(Sprite.invisible()),
                new RotateSpriteComponent(),
                new PhysicsComponent(Vector.zero()));

        environment.addEntity(body)
                .addSystem(new RotateSpriteSystem());

        environment.update();

        var rotation = body.get(RenderComponent.class).options.rotation();
        assertThat(rotation.isNone()).isTrue();
    }

}
