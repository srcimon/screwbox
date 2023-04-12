package io.github.srcimon.screwbox.core.graphics.transitions;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.graphics.Screen;

import static io.github.srcimon.screwbox.core.graphics.Color.BLACK;

public class FadingScreenTransition implements ScreenTransition {

    private static final long serialVersionUID = 1L;

    @Override
    public void draw(final Screen screen, final Percent progress) {
        screen.fillWith(BLACK.opacity(progress.invert()));
    }

}
