package dev.screwbox.core.environment.core;

import dev.screwbox.core.Engine;
import dev.screwbox.core.assets.FontBundle;
import dev.screwbox.core.environment.internal.DefaultEnvironment;
import dev.screwbox.core.graphics.Canvas;
import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.test.EnvironmentExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static dev.screwbox.core.Percent.threeQuarter;
import static dev.screwbox.core.graphics.options.TextDrawOptions.font;
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
