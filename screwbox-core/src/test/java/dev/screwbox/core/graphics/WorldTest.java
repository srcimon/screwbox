package dev.screwbox.core.graphics;

import dev.screwbox.core.Line;
import dev.screwbox.core.graphics.options.LineDrawOptions;
import org.junit.jupiter.api.Test;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoSettings;

import static dev.screwbox.core.Vector.$;
import static dev.screwbox.core.graphics.Color.RED;
import static org.mockito.Mockito.verify;

@MockitoSettings
class WorldTest {

    @Spy
    World world;

    @Test
    void drawLine_lineGiven_drawsWithEndpoints() {
        world.drawLine(Line.between($(10, 20), $(11, 900)), LineDrawOptions.color(RED));

        verify(world).drawLine($(10, 20), $(11, 900), LineDrawOptions.color(RED));
    }
}
