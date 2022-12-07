package de.suzufa.screwbox.core.entities.systems;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.ScrewBox;
import de.suzufa.screwbox.core.Vector;
import de.suzufa.screwbox.core.entities.Entity;
import de.suzufa.screwbox.core.entities.components.AutoFlipSpriteComponent;
import de.suzufa.screwbox.core.entities.components.PhysicsBodyComponent;
import de.suzufa.screwbox.core.entities.components.SpriteComponent;
import de.suzufa.screwbox.core.graphics.Flip;

class AutoFlipSpriteSystemTest {

    @Test
    void update_updatesSpriteFlipsAccordingToMovement() {
        Engine engine = ScrewBox.createHeadlessEngine();

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

        engine.entities()
                .add(movingRight, movingLeftUp, movingDown)
                .add(new StopEngineOnMaxFramesSystem(1))
                .add(new AutoFlipSpriteSystem());

        engine.start();

        assertThat(movingRight.get(SpriteComponent.class).flip).isEqualTo(Flip.NONE);
        assertThat(movingLeftUp.get(SpriteComponent.class).flip).isEqualTo(Flip.HORIZONTAL);
        assertThat(movingDown.get(SpriteComponent.class).flip).isEqualTo(Flip.NONE);
    }

}
