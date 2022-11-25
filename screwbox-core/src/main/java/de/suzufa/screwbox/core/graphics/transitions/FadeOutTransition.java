package de.suzufa.screwbox.core.graphics.transitions;

import de.suzufa.screwbox.core.Percent;
import de.suzufa.screwbox.core.graphics.Screen;

public class FadeOutTransition implements ScreenTransition {

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
