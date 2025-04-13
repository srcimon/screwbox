package dev.screwbox.core.environment.core;

import dev.screwbox.core.Time;
import dev.screwbox.core.environment.internal.DefaultEnvironment;
import dev.screwbox.core.graphics.Canvas;
import dev.screwbox.core.loop.Loop;
import dev.screwbox.core.test.EnvironmentExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.function.Supplier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(EnvironmentExtension.class)
class CrtMonitorSystemTest {

    @Test
    void update_doesntDecorateSplitScreen_drawsDecorations(DefaultEnvironment environment, Loop loop, Canvas canvas) {
        when(loop.time()).thenReturn(Time.atNanos(1239));
        when(canvas.height()).thenReturn(30);
        environment.addSystem(new CrtMonitorOverlaySystem(false));

        environment.update();

        verify(canvas, times(10)).drawLine(any(), any(), any());
        verify(canvas, times(4)).drawSprite(any(Supplier.class), any(), any());
    }
}
