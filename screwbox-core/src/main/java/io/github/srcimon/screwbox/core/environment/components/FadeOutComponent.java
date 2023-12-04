package io.github.srcimon.screwbox.core.environment.components;

import io.github.srcimon.screwbox.core.environment.Component;

import java.io.Serial;

public class FadeOutComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public final double speed;

    public FadeOutComponent(final double speed) {
        this.speed = speed;
    }
}
