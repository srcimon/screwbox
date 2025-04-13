package dev.screwbox.core.scenes.animations;

import dev.screwbox.core.Percent;
import dev.screwbox.core.graphics.Canvas;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.graphics.options.CircleDrawOptions;
import dev.screwbox.core.scenes.Animation;

/**
 * Fills the {@link Canvas} with multiple circles growing in size.
 */
public class CirclesAnimation implements Animation {

    @Override
    public void draw(final Canvas canvas, final Percent progress) {
        final int size = Math.max(canvas.width(), canvas.height()) / 20;
        final int xDelta = canvas.width() / (canvas.width() / size);
        final int yDelta = canvas.height() / (canvas.height() / size);
        final var options = CircleDrawOptions.filled(Color.BLACK);

        for (int x = 0; x < canvas.width() + xDelta; x += xDelta) {
            for (int y = 0; y < canvas.height() + yDelta; y += yDelta) {
                canvas.drawCircle(Offset.at(x, y), (int) (progress.value() * size), options);
            }
        }
    }
}
