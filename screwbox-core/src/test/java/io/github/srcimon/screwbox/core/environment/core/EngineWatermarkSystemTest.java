package io.github.srcimon.screwbox.core.environment.core;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.assets.FontBundle;
import io.github.srcimon.screwbox.core.environment.internal.DefaultEnvironment;
import io.github.srcimon.screwbox.core.graphics.Canvas;
import io.github.srcimon.screwbox.core.graphics.Offset;
import io.github.srcimon.screwbox.core.test.EnvironmentExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static io.github.srcimon.screwbox.core.Percent.threeQuarter;
import static io.github.srcimon.screwbox.core.graphics.drawoptions.TextDrawOptions.font;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(EnvironmentExtension.class)
class EngineWatermarkSystemTest {

    @Test
    void update_drawsWatermark(Engine engine, Canvas canvas, DefaultEnvironment environment) {
        when(canvas.height()).thenReturn(640);
        when(engine.version()).thenReturn("1.2.3");
        environment.addSystem(new EngineWatermarkSystem());

        environment.update();

        verify(canvas).drawText(
                Offset.at(14, 626),
                "Running on ScrewBox game engine - version 1.2.3 - visit ScrewBox.dev",
                font(FontBundle.BOLDZILLA).opacity(threeQuarter()));
    }
}
