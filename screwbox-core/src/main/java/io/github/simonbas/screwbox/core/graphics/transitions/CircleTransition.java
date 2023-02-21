package io.github.simonbas.screwbox.core.graphics.transitions;

import io.github.simonbas.screwbox.core.Percent;
import io.github.simonbas.screwbox.core.graphics.Color;
import io.github.simonbas.screwbox.core.graphics.Offset;
import io.github.simonbas.screwbox.core.graphics.Screen;

public class CircleTransition implements ScreenTransition {

    private static final long serialVersionUID = 1L;

    @Override
    public void draw(final Screen screen, final Percent progress) {
        final double width = screen.size().width();
        final double height = screen.size().height();
        final double maxRadius = Math.sqrt(width * width + height * height);
        final Offset offset = Offset.at(width / 2.0, height / 2.0);
        final int diameter = (int) (maxRadius * progress.invert().value());
        screen.fillCircle(offset, diameter, Color.BLACK);
    }
}