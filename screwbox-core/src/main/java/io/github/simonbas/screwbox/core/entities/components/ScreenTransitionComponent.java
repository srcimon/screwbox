package io.github.simonbas.screwbox.core.entities.components;

import io.github.simonbas.screwbox.core.Duration;
import io.github.simonbas.screwbox.core.Time;
import io.github.simonbas.screwbox.core.entities.Component;
import io.github.simonbas.screwbox.core.graphics.transitions.ScreenTransition;

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
