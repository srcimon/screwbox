package dev.screwbox.platformer.components;

import dev.screwbox.core.Duration;
import dev.screwbox.core.Time;
import dev.screwbox.core.environment.Component;

public class VanishingOnCollisionComponent implements Component {

    private static final long serialVersionUID = 1L;

    public Duration timeout;
    public Time vanishTime = Time.unset();

    public VanishingOnCollisionComponent(final Duration timeout) {
        this.timeout = timeout;
    }
}
