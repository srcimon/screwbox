package io.github.srcimon.screwbox.core.environment.tweening;

import io.github.srcimon.screwbox.core.environment.Component;

import java.io.Serial;

public class TweenXPositionComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public double from;
    public double to;

    public TweenXPositionComponent(final double from, final double to) {
        this.from = from;
        this.to = to;
    }
}
