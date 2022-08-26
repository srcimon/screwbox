package de.suzufa.screwbox.core.entityengine.systems;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import de.suzufa.screwbox.core.Vector;
import de.suzufa.screwbox.core.entityengine.Entity;
import de.suzufa.screwbox.core.entityengine.components.AutoRotationComponent;
import de.suzufa.screwbox.core.entityengine.components.PhysicsBodyComponent;
import de.suzufa.screwbox.core.entityengine.components.SpriteComponent;
import de.suzufa.screwbox.core.entityengine.internal.DefaultEntityEngine;
import de.suzufa.screwbox.core.graphics.Sprite;
import de.suzufa.screwbox.core.test.EntityEngineExtension;

@ExtendWith(EntityEngineExtension.class)
class AutoRotationSystemTest {

    @Test
    void update_rotationNonZero_updatesSpriteRotation(DefaultEntityEngine entityEngine) {
        Entity body = new Entity().add(
                new SpriteComponent(Sprite.invisible()),
                new AutoRotationComponent(),
                new PhysicsBodyComponent(Vector.of(4, 4)));

        entityEngine.add(body)
                .add(new AutoRotationSystem());

        entityEngine.update();

        var rotation = body.get(SpriteComponent.class).rotation;
        assertThat(rotation.degrees()).isEqualTo(135);
    }

    @Test
    void update_rotationIsZero_doesntUpdateSpriteRotation(DefaultEntityEngine entityEngine) {
        Entity body = new Entity().add(
                new SpriteComponent(Sprite.invisible()),
                new AutoRotationComponent(),
                new PhysicsBodyComponent(Vector.zero()));

        entityEngine.add(body)
                .add(new AutoRotationSystem());

        entityEngine.update();

        var rotation = body.get(SpriteComponent.class).rotation;
        assertThat(rotation.isNone()).isTrue();
    }

}
