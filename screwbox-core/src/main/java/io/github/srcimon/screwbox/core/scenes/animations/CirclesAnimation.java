package io.github.srcimon.screwbox.core.scenes.animations;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.graphics.drawoptions.CircleDrawOptions;
import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.Offset;
import io.github.srcimon.screwbox.core.graphics.Screen;
import io.github.srcimon.screwbox.core.scenes.Animation;

/**
 * Fills the {@link Screen} with multiple circles growin in size.
 */
public class CirclesAnimation implements Animation {

    @Override
    public void draw(final Screen screen, final Percent progress) {
        int size = Math.max(screen.width(), screen.height()) / 20;
        int xDelta = screen.width() / (screen.width() / size);
        int yDelta = screen.height() / (screen.height() / size);

        for (int x = 0; x < screen.width() + xDelta; x += xDelta) {
            for (int y = 0; y < screen.height() + yDelta; y += yDelta) {
                screen.drawCircle(Offset.at(x, y), (int) (progress.value() * size), CircleDrawOptions.filled(Color.BLACK));
            }
        }
    }
}
