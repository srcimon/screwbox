package de.suzufa.screwbox.core.graphics.transitions;

import static de.suzufa.screwbox.core.graphics.Offset.origin;
import static de.suzufa.screwbox.core.graphics.window.WindowRectangle.rectangle;

import de.suzufa.screwbox.core.Percentage;
import de.suzufa.screwbox.core.graphics.Color;
import de.suzufa.screwbox.core.graphics.window.Window;

public class FadingScreenTransition implements ScreenTransition {

    private static final long serialVersionUID = 1L;

    @Override
    public void draw(final Window screen, final Percentage progress) {
        screen.draw(rectangle(origin(), screen.size(), Color.BLACK, progress.invert()));
    }

}
