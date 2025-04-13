package dev.screwbox.core.scenes;

import dev.screwbox.core.environment.internal.DefaultEnvironment;
import dev.screwbox.core.graphics.Canvas;
import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.graphics.options.TextDrawOptions;
import dev.screwbox.core.test.EnvironmentExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(EnvironmentExtension.class)
class DefaultLoadingSceneTest {

    @Test
    void runDefaultLoadingScene_onUpdate_drawsFifteenCharacters(DefaultEnvironment environment, Canvas canvas) {
        new DefaultLoadingScene().populate(environment);
        when(canvas.center()).thenReturn(Offset.at(120, 50));

        environment.update();

        verify(canvas, times(15)).drawText(any(), any(), any(TextDrawOptions.class));
    }
}
