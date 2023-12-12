package io.github.srcimon.screwbox.core.scenes;

import io.github.srcimon.screwbox.core.environment.internal.DefaultEnvironment;
import io.github.srcimon.screwbox.core.graphics.Offset;
import io.github.srcimon.screwbox.core.graphics.Screen;
import io.github.srcimon.screwbox.core.test.EnvironmentExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.Mockito.*;

@ExtendWith(EnvironmentExtension.class)
class DefaultLoadingSceneTest {

    @Test
    void runDefaultLoadingScene_onUpdate_drawsThreeRectangles(DefaultEnvironment entities, Screen screen) {
        new DefaultLoadingScene().populate(entities);
        when(screen.center()).thenReturn(Offset.at(120, 50));

        entities.update();

        verify(screen, times(3)).fillRectangle(any(), any(), any());
    }
}
