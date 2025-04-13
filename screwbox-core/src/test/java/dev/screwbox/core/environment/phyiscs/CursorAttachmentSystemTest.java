package dev.screwbox.core.environment.phyiscs;

import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.core.TransformComponent;
import dev.screwbox.core.environment.internal.DefaultEnvironment;
import dev.screwbox.core.environment.physics.CursorAttachmentComponent;
import dev.screwbox.core.environment.physics.CursorAttachmentSystem;
import dev.screwbox.core.mouse.Mouse;
import dev.screwbox.core.test.EnvironmentExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static dev.screwbox.core.Vector.$;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(EnvironmentExtension.class)
class CursorAttachmentSystemTest {

    @Test
    void update_changesPositionOfCursorAttachedEntity(DefaultEnvironment environment, Mouse mouse) {
        when(mouse.position()).thenReturn($(20, 40));

        environment
                .addEntity(1, new TransformComponent(Vector.zero(), 16, 16), new CursorAttachmentComponent())
                .addEntity(2, new TransformComponent(Vector.zero(), 16, 16), new CursorAttachmentComponent($(10, 4)))
                .addSystem(new CursorAttachmentSystem());

        environment.update();

        assertThat(environment.fetchById(1).position()).isEqualTo($(20, 40));
        assertThat(environment.fetchById(2).position()).isEqualTo($(30, 44));
    }
}
