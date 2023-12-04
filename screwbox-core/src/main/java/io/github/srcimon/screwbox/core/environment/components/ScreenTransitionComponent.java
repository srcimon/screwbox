package io.github.srcimon.screwbox.core.environment.components;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Time;
import io.github.srcimon.screwbox.core.environment.Component;
import io.github.srcimon.screwbox.core.graphics.transitions.ScreenTransition;

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
