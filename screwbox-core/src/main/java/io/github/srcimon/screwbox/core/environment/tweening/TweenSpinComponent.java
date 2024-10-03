package io.github.srcimon.screwbox.core.environment.tweening;

import io.github.srcimon.screwbox.core.environment.Component;

import java.io.Serial;

//TODO javadoc
//TODO changelog
//TODO test
public class TweenSpinComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public double horizontal;
    public double vertical;

    public TweenSpinComponent() {
        this(1);
    }

    public TweenSpinComponent(final double horizontal) {
        this(horizontal, 0);
    }

    public TweenSpinComponent(final double horizontal, final double vertical) {
        this.horizontal = horizontal;
        this.vertical = vertical;
    }
}
