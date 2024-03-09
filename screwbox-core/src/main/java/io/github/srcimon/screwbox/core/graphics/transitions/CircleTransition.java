package io.github.srcimon.screwbox.core.graphics.transitions;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.graphics.CircleDrawOptions;
import io.github.srcimon.screwbox.core.graphics.Offset;
import io.github.srcimon.screwbox.core.graphics.Screen;

import java.io.Serial;

import static io.github.srcimon.screwbox.core.graphics.Color.BLACK;

public class CircleTransition implements ScreenTransition {

    @Serial
    private static final long serialVersionUID = 1L;

    @Override
    public void draw(final Screen screen, final Percent progress) {
        final double width = screen.size().width();
        final double height = screen.size().height();
        final double maxRadius = Math.sqrt(width * width + height * height) / 2.0;
        final Offset offset = Offset.at(width / 2.0, height / 2.0);
        final int radius = (int) (maxRadius * progress.invert().value());
        screen.drawCircle(offset, radius, CircleDrawOptions.filled(BLACK));
    }
}