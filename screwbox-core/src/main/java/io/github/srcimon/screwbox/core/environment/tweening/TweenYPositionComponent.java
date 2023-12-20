package io.github.srcimon.screwbox.core.environment.tweening;

import io.github.srcimon.screwbox.core.environment.Component;

import java.io.Serial;

public class TweenYPositionComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public double from;
    public double to;

    public TweenYPositionComponent(final double from, final double to) {
        this.from = from;
        this.to = to;
    }
}
