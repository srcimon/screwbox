package de.suzufa.screwbox.core.graphics.transitions;

import static de.suzufa.screwbox.core.graphics.Color.BLACK;

import de.suzufa.screwbox.core.Percentage;
import de.suzufa.screwbox.core.graphics.Window;

public class FadingScreenTransition implements ScreenTransition {

    private static final long serialVersionUID = 1L;

    @Override
    public void draw(final Window screen, final Percentage progress) {
        screen.fillWith(BLACK.withOpacity(progress.invert()));
    }

}
