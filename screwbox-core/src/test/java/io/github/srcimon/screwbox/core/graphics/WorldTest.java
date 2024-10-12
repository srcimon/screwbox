package io.github.srcimon.screwbox.core.graphics;

import io.github.srcimon.screwbox.core.Line;
import io.github.srcimon.screwbox.core.graphics.drawoptions.LineDrawOptions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static io.github.srcimon.screwbox.core.Vector.$;
import static io.github.srcimon.screwbox.core.graphics.Color.RED;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class WorldTest {

    @Spy
    World world;

    @Test
    void drawLine_lineGiven_drawsWithEndpoints() {
        world.drawLine(Line.between($(10, 20), $(11, 900)), LineDrawOptions.color(RED));

        verify(world).drawLine($(10, 20), $(11, 900), LineDrawOptions.color(RED));
    }
}
