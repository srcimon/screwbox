package dev.screwbox.core.environment.rendering;

import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.internal.DefaultEnvironment;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.core.test.EnvironmentExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(EnvironmentExtension.class)
class FlipSpriteSystemTest {

    @Test
    void update_updatesSpriteComponentFlipMode(DefaultEnvironment environment) {
        Entity movingRight = new Entity()
                .add(new RenderComponent())
                .add(new PhysicsComponent(Vector.x(4)))
                .add(new FlipSpriteComponent());

        Entity movingLeftUp = new Entity()
                .add(new RenderComponent())
                .add(new PhysicsComponent(Vector.of(-4, -2)))
                .add(new FlipSpriteComponent());

        Entity movingDown = new Entity()
                .add(new RenderComponent())
                .add(new PhysicsComponent(Vector.y(1)))
                .add(new FlipSpriteComponent());

        environment.addEntities(movingRight, movingLeftUp, movingDown)
                .addSystem(new FlipSpriteSystem());

        environment.update();

        assertThat(movingRight.get(RenderComponent.class).options.isFlipHorizontal()).isFalse();
        assertThat(movingLeftUp.get(RenderComponent.class).options.isFlipHorizontal()).isTrue();
        assertThat(movingDown.get(RenderComponent.class).options.isFlipHorizontal()).isFalse();
    }

}
