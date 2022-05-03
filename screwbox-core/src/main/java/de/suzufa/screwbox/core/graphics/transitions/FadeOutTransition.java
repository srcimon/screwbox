package de.suzufa.screwbox.core.graphics.transitions;

import de.suzufa.screwbox.core.Percentage;
import de.suzufa.screwbox.core.graphics.window.Window;

public class FadeOutTransition implements ScreenTransition {

    private static final long serialVersionUID = 1L;

    private final ScreenTransition transition;

    public FadeOutTransition(final ScreenTransition transition) {
        this.transition = transition;
    }

    @Override
    public void draw(final Window window, final Percentage progress) {
        final Percentage inverted = Percentage.of(Percentage.max().value() - progress.value());
        transition.draw(window, inverted);
    }

}
