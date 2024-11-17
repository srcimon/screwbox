package io.github.srcimon.screwbox.core.environment.core;

import io.github.srcimon.screwbox.core.Time;
import io.github.srcimon.screwbox.core.environment.internal.DefaultEnvironment;
import io.github.srcimon.screwbox.core.graphics.Canvas;
import io.github.srcimon.screwbox.core.loop.Loop;
import io.github.srcimon.screwbox.core.test.EnvironmentExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.function.Supplier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(EnvironmentExtension.class)
public class CrtMonitorSystemTest {

    @Test
    void update_doesntDecorateSplitScreen_drawsDecorations(DefaultEnvironment environment, Loop loop, Canvas canvas) {
        when(loop.lastUpdate()).thenReturn(Time.atNanos(1239));
        when(canvas.height()).thenReturn(30);
        environment.addSystem(new CrtMonitorOverlaySystem(false));

        environment.update();

        verify(canvas, times(10)).drawLine(any(), any(), any());
        verify(canvas, times(4)).drawSprite(any(Supplier.class), any(), any());
    }
}
