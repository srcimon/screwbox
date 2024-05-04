package io.github.srcimon.screwbox.core.scenes.animations;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.graphics.CircleDrawOptions;
import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.Offset;
import io.github.srcimon.screwbox.core.graphics.Screen;
import io.github.srcimon.screwbox.core.scenes.Animation;

public class CirclesAnimation implements Animation {

    @Override
    public void draw(final Screen screen, final Percent progress) {
        int size = Math.max(screen.size().width(), screen.size().height()) / 20;
        int xDelta = screen.size().width() / (screen.size().width() / size);
        int yDelta = screen.size().height() / (screen.size().height() / size);

        for (int x = 0; x < screen.size().width() + xDelta; x += xDelta) {
            for (int y = 0; y < screen.size().height() + yDelta; y += yDelta) {
                screen.drawCircle(Offset.at(x, y), (int) (progress.value() * size), CircleDrawOptions.filled(Color.BLACK));
            }
        }
    }
}
