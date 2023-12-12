package io.github.srcimon.screwbox.core.environment.systems;

import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.components.AutoFlipSpriteComponent;
import io.github.srcimon.screwbox.core.environment.components.PhysicsBodyComponent;
import io.github.srcimon.screwbox.core.environment.components.RenderComponent;
import io.github.srcimon.screwbox.core.environment.internal.DefaultEnvironment;
import io.github.srcimon.screwbox.core.graphics.Flip;
import io.github.srcimon.screwbox.core.test.EnvironmentExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(EnvironmentExtension.class)
class AutFlipSpriteSystemTest {

    @Test
    void update_updatesSpritComponentFlipMode(DefaultEnvironment environment) {
        Entity movingRight = new Entity()
                .add(new RenderComponent())
                .add(new PhysicsBodyComponent(Vector.x(4)))
                .add(new AutoFlipSpriteComponent());

        Entity movingLeftUp = new Entity()
                .add(new RenderComponent())
                .add(new PhysicsBodyComponent(Vector.of(-4, -2)))
                .add(new AutoFlipSpriteComponent());

        Entity movingDown = new Entity()
                .add(new RenderComponent())
                .add(new PhysicsBodyComponent(Vector.y(1)))
                .add(new AutoFlipSpriteComponent());

        environment.addSystem(movingRight, movingLeftUp, movingDown)
                .addSystem(new AutoFlipSpriteSystem());

        environment.update();

        assertThat(movingRight.get(RenderComponent.class).flip).isEqualTo(Flip.NONE);
        assertThat(movingLeftUp.get(RenderComponent.class).flip).isEqualTo(Flip.HORIZONTAL);
        assertThat(movingDown.get(RenderComponent.class).flip).isEqualTo(Flip.NONE);
    }

}
