package de.suzufa.screwbox.core.entityengine.components;

import de.suzufa.screwbox.core.Duration;
import de.suzufa.screwbox.core.Time;
import de.suzufa.screwbox.core.entityengine.Component;
import de.suzufa.screwbox.core.graphics.transitions.ScreenTransition;

public class ScreenTransitionComponent implements Component {

    private static final long serialVersionUID = 1L;

    public final ScreenTransition transition;
    public final Duration duration;
    public Time startTime;

    public ScreenTransitionComponent(final ScreenTransition transition, final Duration duration) {
        this.transition = transition;
        this.duration = duration;
    }

}
