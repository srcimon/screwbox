package io.github.srcimon.screwbox.core.environment.tweening;

import io.github.srcimon.screwbox.core.environment.Component;

import java.io.Serial;

public class TweenScaleComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public final double from;
    public final double to;

    public TweenScaleComponent(double from, double to) {
        this.from = from;
        this.to = to;
    }
}
