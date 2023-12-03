package io.github.srcimon.screwbox.core.entities.systems;

import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.entities.Entity;
import io.github.srcimon.screwbox.core.entities.components.AutoRotationComponent;
import io.github.srcimon.screwbox.core.entities.components.PhysicsBodyComponent;
import io.github.srcimon.screwbox.core.entities.components.RenderComponent;
import io.github.srcimon.screwbox.core.entities.internal.DefaultEntities;
import io.github.srcimon.screwbox.core.graphics.Sprite;
import io.github.srcimon.screwbox.core.test.EntitiesExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(EntitiesExtension.class)
class AutoRotationSystemTest {

    @Test
    void update_rotationNonZero_updatesSpriteRotation(DefaultEntities entities) {
        Entity body = new Entity().add(
                new RenderComponent(Sprite.invisible()),
                new AutoRotationComponent(),
                new PhysicsBodyComponent(Vector.of(4, 4)));

        entities.addEntity(body)
                .addSystem(new AutoRotationSystem());

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

        entities.addEntity(body)
                .addSystem(new AutoRotationSystem());

        entities.update();

        var rotation = body.get(RenderComponent.class).rotation;
        assertThat(rotation.isNone()).isTrue();
    }

}
