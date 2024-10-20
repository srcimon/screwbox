package io.github.srcimon.screwbox.core.scenes.animations;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.graphics.Canvas;
import io.github.srcimon.screwbox.core.graphics.drawoptions.CircleDrawOptions;
import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.Offset;
import io.github.srcimon.screwbox.core.scenes.Animation;

/**
 * Fills the {@link Canvas} with multiple circles growin in size.
 */
public class CirclesAnimation implements Animation {

    @Override
    public void draw(final Canvas canvas, final Percent progress) {
        int size = Math.max(canvas.width(), canvas.height()) / 20;
        int xDelta = canvas.width() / (canvas.width() / size);
        int yDelta = canvas.height() / (canvas.height() / size);

        for (int x = canvas.offset().x(); x < canvas.width() + xDelta + canvas.offset().x(); x += xDelta) {
            for (int y = canvas.offset().y(); y < canvas.height() + yDelta + canvas.offset().y(); y += yDelta) {
                canvas.drawCircle(Offset.at(x, y), (int) (progress.value() * size), CircleDrawOptions.filled(Color.BLACK));
            }
        }
    }
}
