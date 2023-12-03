package io.github.srcimon.screwbox.core.scenes;

import io.github.srcimon.screwbox.core.ecosphere.internal.DefaultEcosphere;
import io.github.srcimon.screwbox.core.graphics.Offset;
import io.github.srcimon.screwbox.core.graphics.Screen;
import io.github.srcimon.screwbox.core.test.EntitiesExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.Mockito.*;

@ExtendWith(EntitiesExtension.class)
class DefaultLoadingSceneTest {

    @Test
    void runDefaultLoadingScene_onUpdate_drawsThreeRectangles(DefaultEcosphere entities, Screen screen) {
        new DefaultLoadingScene().populate(entities);
        when(screen.center()).thenReturn(Offset.at(120, 50));

        entities.update();

        verify(screen, times(3)).fillRectangle(any(), any(), any());
    }
}
