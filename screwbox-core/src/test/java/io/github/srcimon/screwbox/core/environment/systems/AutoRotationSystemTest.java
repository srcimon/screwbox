package io.github.srcimon.screwbox.core.environment.systems;

import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.components.AutoRotationComponent;
import io.github.srcimon.screwbox.core.environment.physics.RigidBodyComponent;
import io.github.srcimon.screwbox.core.environment.components.RenderComponent;
import io.github.srcimon.screwbox.core.environment.internal.DefaultEnvironment;
import io.github.srcimon.screwbox.core.graphics.Sprite;
import io.github.srcimon.screwbox.core.test.EnvironmentExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(EnvironmentExtension.class)
class AutoRotationSystemTest {

    @Test
    void update_rotationNonZero_updatesSpriteRotation(DefaultEnvironment environment) {
        Entity body = new Entity().add(
                new RenderComponent(Sprite.invisible()),
                new AutoRotationComponent(),
                new RigidBodyComponent(Vector.of(4, 4)));

        environment.addEntity(body)
                .addSystem(new AutoRotationSystem());

        environment.update();

        var rotation = body.get(RenderComponent.class).rotation;
        assertThat(rotation.degrees()).isEqualTo(135);
    }

    @Test
    void update_rotationIsZero_doesntUpdateSpriteRotation(DefaultEnvironment environment) {
        Entity body = new Entity().add(
                new RenderComponent(Sprite.invisible()),
                new AutoRotationComponent(),
                new RigidBodyComponent(Vector.zero()));

        environment.addEntity(body)
                .addSystem(new AutoRotationSystem());

        environment.update();

        var rotation = body.get(RenderComponent.class).rotation;
        assertThat(rotation.isNone()).isTrue();
    }

}
