package dev.screwbox.core.environment.phyiscs;

import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.core.TransformComponent;
import dev.screwbox.core.environment.internal.DefaultEnvironment;
import dev.screwbox.core.environment.physics.AttachmentComponent;
import dev.screwbox.core.environment.physics.AttachmentSystem;
import dev.screwbox.core.test.EnvironmentExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static dev.screwbox.core.Vector.$;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(EnvironmentExtension.class)
class AttachmentSystemTest {

    @Test
    void update_targetEntityFound_movesToTarget(DefaultEnvironment environment) {
        var attached = new Entity()
                .add(new AttachmentComponent(47, $(4, 5)))
                .add(new TransformComponent(0, 0, 20, 20));

        environment
                .addEntity(attached)
                .addEntity(47, new TransformComponent(100, 1000, 20, 20))
                .addSystem(new AttachmentSystem());

        environment.update();

        assertThat(attached.position()).isEqualTo($(104, 1005));
    }

    @Test
    void update_targetEntityMissing_notMoved(DefaultEnvironment environment) {
        var attached = new Entity()
                .add(new AttachmentComponent(47, $(4, 5)))
                .add(new TransformComponent(0, 0, 20, 20));

        environment
                .addEntity(attached)
                .addSystem(new AttachmentSystem());

        environment.update();

        assertThat(attached.position()).isEqualTo($(0, 0));
    }
}
