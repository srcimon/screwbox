package io.github.srcimon.screwbox.core.graphics.transitions;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.graphics.Screen;

import java.io.Serial;

public class FadeOutTransition implements ScreenTransition {

    @Serial
    private static final long serialVersionUID = 1L;

    private final ScreenTransition transition;

    public FadeOutTransition(final ScreenTransition transition) {
        this.transition = transition;
    }

    @Override
    public void draw(final Screen screen, final Percent progress) {
        transition.draw(screen, progress.invert());
    }

}
