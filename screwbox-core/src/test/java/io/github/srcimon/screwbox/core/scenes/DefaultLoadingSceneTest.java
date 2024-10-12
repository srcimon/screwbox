package io.github.srcimon.screwbox.core.scenes;

import io.github.srcimon.screwbox.core.environment.internal.DefaultEnvironment;
import io.github.srcimon.screwbox.core.graphics.Offset;
import io.github.srcimon.screwbox.core.graphics.Screen;
import io.github.srcimon.screwbox.core.graphics.drawoptions.TextDrawOptions;
import io.github.srcimon.screwbox.core.test.EnvironmentExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(EnvironmentExtension.class)
class DefaultLoadingSceneTest {

    @Test
    void runDefaultLoadingScene_onUpdate_drawsFiveteenCharacters(DefaultEnvironment environment, Screen screen) {
        new DefaultLoadingScene().populate(environment);
        when(screen.center()).thenReturn(Offset.at(120, 50));

        environment.update();

        verify(screen, times(15)).drawText(any(), any(), any(TextDrawOptions.class));
    }
}
