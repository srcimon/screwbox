package io.github.srcimon.screwbox.core.environment.phyiscs;

import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;
import io.github.srcimon.screwbox.core.environment.internal.DefaultEnvironment;
import io.github.srcimon.screwbox.core.environment.physics.CursorAttachmentComponent;
import io.github.srcimon.screwbox.core.environment.physics.CursorAttachmentSystem;
import io.github.srcimon.screwbox.core.mouse.Mouse;
import io.github.srcimon.screwbox.core.test.EnvironmentExtension;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;

import static io.github.srcimon.screwbox.core.Vector.$;
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
