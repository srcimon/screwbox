package de.suzufa.screwbox.core.entities.systems;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import de.suzufa.screwbox.core.Vector;
import de.suzufa.screwbox.core.entities.Entity;
import de.suzufa.screwbox.core.entities.components.AutoRotationComponent;
import de.suzufa.screwbox.core.entities.components.PhysicsBodyComponent;
import de.suzufa.screwbox.core.entities.components.RenderComponent;
import de.suzufa.screwbox.core.entities.internal.DefaultEntities;
import de.suzufa.screwbox.core.graphics.Sprite;
import de.suzufa.screwbox.core.test.EntitiesExtension;

@ExtendWith(EntitiesExtension.class)
class AutoRotationSystemTest {

    @Test
    void update_rotationNonZero_updatesSpriteRotation(DefaultEntities entities) {
        Entity body = new Entity().add(
                new RenderComponent(Sprite.invisible()),
                new AutoRotationComponent(),
                new PhysicsBodyComponent(Vector.of(4, 4)));

        entities.add(body)
                .add(new AutoRotationSystem());

        entities.update();

        var rotation = body.get(RenderComponent.class).rotation;
        assertThat(rotation.degrees()).isEqualTo(135);
    }

    @Test
    void update_rotationIsZero_doesntUpdateSpriteRotation(DefaultEntities entities) {
        Entity body = new Entity().add(
                new RenderComponent(Sprite.invisible()),
                new AutoRotationComponent(),
                new PhysicsBodyComponent(Vector.zero()));

        entities.add(body)
                .add(new AutoRotationSystem());

        entities.update();

        var rotation = body.get(RenderComponent.class).rotation;
        assertThat(rotation.isNone()).isTrue();
    }

}
