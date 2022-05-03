package de.suzufa.screwbox.core.graphics.transitions;

import static de.suzufa.screwbox.core.graphics.window.WindowCircle.circle;

import de.suzufa.screwbox.core.Percentage;
import de.suzufa.screwbox.core.graphics.Color;
import de.suzufa.screwbox.core.graphics.Offset;
import de.suzufa.screwbox.core.graphics.Window;

public class CircleTransition implements ScreenTransition {

    private static final long serialVersionUID = 1L;

    @Override
    public void draw(final Window window, final Percentage progress) {
        final double width = window.size().width();
        final double height = window.size().height();
        final double maxRadius = Math.sqrt(width * width + height * height);
        final Offset offset = Offset.at(width / 2.0, height / 2.0);
        final int diameter = (int) (maxRadius * progress.invert().value());
        window.draw(circle(offset, diameter, Color.BLACK));
    }

}
