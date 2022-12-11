package de.suzufa.screwbox.core.entities.systems;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import de.suzufa.screwbox.core.Vector;
import de.suzufa.screwbox.core.entities.Entity;
import de.suzufa.screwbox.core.entities.components.AutoFlipSpriteComponent;
import de.suzufa.screwbox.core.entities.components.PhysicsBodyComponent;
import de.suzufa.screwbox.core.entities.components.RenderComponent;
import de.suzufa.screwbox.core.entities.internal.DefaultEntities;
import de.suzufa.screwbox.core.graphics.Flip;
import de.suzufa.screwbox.core.test.EntitiesExtension;

@ExtendWith(EntitiesExtension.class)
class AutFlipSpriteSystemTest {

    @Test
    void update_updatesSpritComponentFlipMode(DefaultEntities entities) {
        Entity movingRight = new Entity()
                .add(new RenderComponent())
                .add(new PhysicsBodyComponent(Vector.xOnly(4)))
                .add(new AutoFlipSpriteComponent());

        Entity movingLeftUp = new Entity()
                .add(new RenderComponent())
                .add(new PhysicsBodyComponent(Vector.of(-4, -2)))
                .add(new AutoFlipSpriteComponent());

        Entity movingDown = new Entity()
                .add(new RenderComponent())
                .add(new PhysicsBodyComponent(Vector.yOnly(1)))
                .add(new AutoFlipSpriteComponent());

        entities.add(movingRight, movingLeftUp, movingDown)
                .add(new AutoFlipSpriteSystem());

        entities.update();

        assertThat(movingRight.get(RenderComponent.class).flip).isEqualTo(Flip.NONE);
        assertThat(movingLeftUp.get(RenderComponent.class).flip).isEqualTo(Flip.HORIZONTAL);
        assertThat(movingDown.get(RenderComponent.class).flip).isEqualTo(Flip.NONE);
    }

}
