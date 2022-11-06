package de.suzufa.screwbox.core.entities.systems;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import de.suzufa.screwbox.core.Vector;
import de.suzufa.screwbox.core.entities.Entity;
import de.suzufa.screwbox.core.entities.components.AutoFlipSpriteComponent;
import de.suzufa.screwbox.core.entities.components.PhysicsBodyComponent;
import de.suzufa.screwbox.core.entities.components.SpriteComponent;
import de.suzufa.screwbox.core.entities.internal.DefaultEntities;
import de.suzufa.screwbox.core.graphics.Flip;
import de.suzufa.screwbox.core.test.EntitiesExtension;

@ExtendWith(EntitiesExtension.class)
class AutFlipSpriteSystemTest {

    @Test
    void update_updatesSpritComponentFlipMode(DefaultEntities entities) {
        Entity movingRight = new Entity()
                .add(new SpriteComponent())
                .add(new PhysicsBodyComponent(Vector.xOnly(4)))
                .add(new AutoFlipSpriteComponent());

        Entity movingLeftUp = new Entity()
                .add(new SpriteComponent())
                .add(new PhysicsBodyComponent(Vector.of(-4, -2)))
                .add(new AutoFlipSpriteComponent());

        Entity movingDown = new Entity()
                .add(new SpriteComponent())
                .add(new PhysicsBodyComponent(Vector.yOnly(1)))
                .add(new AutoFlipSpriteComponent());

        entities.add(movingRight, movingLeftUp, movingDown)
                .add(new AutFlipSpriteSystem());

        entities.update();

        assertThat(movingRight.get(SpriteComponent.class).flipMode).isEqualTo(Flip.NONE);
        assertThat(movingLeftUp.get(SpriteComponent.class).flipMode).isEqualTo(Flip.HORIZONTAL);
        assertThat(movingDown.get(SpriteComponent.class).flipMode).isEqualTo(Flip.NONE);
    }

}
