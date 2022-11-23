package de.suzufa.screwbox.core.graphics.transitions;

import static de.suzufa.screwbox.core.graphics.Color.BLACK;

import de.suzufa.screwbox.core.Percent;
import de.suzufa.screwbox.core.graphics.Screen;

public class FadingScreenTransition implements ScreenTransition {

    private static final long serialVersionUID = 1L;

    @Override
    public void draw(final Screen screen, final Percent progress) {
        screen.fillWith(BLACK.opacity(progress.invert()));
    }

}
