package io.github.srcimon.screwbox.core.scenes;

import io.github.srcimon.screwbox.core.entities.internal.DefaultEntities;
import io.github.srcimon.screwbox.core.graphics.Offset;
import io.github.srcimon.screwbox.core.graphics.Screen;
import io.github.srcimon.screwbox.core.test.EntitiesExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(EntitiesExtension.class)
class DefaultLoadingSceneTest {

    @Test
    void runDefaultLoadingScene_onUpdate_drawsThreeRectangles(DefaultEntities entities, Screen screen) {
        new DefaultLoadingScene().initialize(entities);
        when(screen.center()).thenReturn(Offset.at(120, 50));

        entities.update();

        verify(screen, times(3)).fillRectangle(any(), any(), any());
    }
}
