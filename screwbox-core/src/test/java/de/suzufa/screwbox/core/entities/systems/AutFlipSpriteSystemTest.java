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
import de.suzufa.screwbox.core.graphics.FlipMode;
import de.suzufa.screwbox.core.test.EntityEngineExtension;

@ExtendWith(EntityEngineExtension.class)
class AutFlipSpriteSystemTest {

    @Test
    void update_updatesSpritComponentFlipMode(DefaultEntities entityEngine) {
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

        entityEngine.add(movingRight, movingLeftUp, movingDown)
                .add(new AutFlipSpriteSystem());

        entityEngine.update();

        assertThat(movingRight.get(SpriteComponent.class).flipMode).isEqualTo(FlipMode.NONE);
        assertThat(movingLeftUp.get(SpriteComponent.class).flipMode).isEqualTo(FlipMode.HORIZONTAL);
        assertThat(movingDown.get(SpriteComponent.class).flipMode).isEqualTo(FlipMode.NONE);
    }

}
