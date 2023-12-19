package io.github.srcimon.screwbox.examples.platformer.components;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Time;
import io.github.srcimon.screwbox.core.environment.Component;

public class VanishingOnCollisionComponent implements Component {

    private static final long serialVersionUID = 1L;

    public Duration timeout;
    public Time vanishTime = Time.unset();

    public VanishingOnCollisionComponent(final Duration timeout) {
        this.timeout = timeout;
    }
}
