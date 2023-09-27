package io.github.srcimon.screwbox.core.entities.systems;

import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.entities.Entity;
import io.github.srcimon.screwbox.core.entities.components.AutoFlipSpriteComponent;
import io.github.srcimon.screwbox.core.entities.components.PhysicsBodyComponent;
import io.github.srcimon.screwbox.core.entities.components.RenderComponent;
import io.github.srcimon.screwbox.core.entities.internal.DefaultEntities;
import io.github.srcimon.screwbox.core.graphics.Flip;
import io.github.srcimon.screwbox.core.test.EntitiesExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(EntitiesExtension.class)
class AutFlipSpriteSystemTest {

    @Test
    void update_updatesSpritComponentFlipMode(DefaultEntities entities) {
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

        entities.add(movingRight, movingLeftUp, movingDown)
                .add(new AutoFlipSpriteSystem());

        entities.update();

        assertThat(movingRight.get(RenderComponent.class).flip).isEqualTo(Flip.NONE);
        assertThat(movingLeftUp.get(RenderComponent.class).flip).isEqualTo(Flip.HORIZONTAL);
        assertThat(movingDown.get(RenderComponent.class).flip).isEqualTo(Flip.NONE);
    }

}
