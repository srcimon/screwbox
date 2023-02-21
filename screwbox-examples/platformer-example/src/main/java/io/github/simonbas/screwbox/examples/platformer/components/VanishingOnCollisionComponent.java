package io.github.simonbas.screwbox.examples.platformer.components;

import io.github.simonbas.screwbox.core.Duration;
import io.github.simonbas.screwbox.core.Time;
import io.github.simonbas.screwbox.core.entities.Component;

public class VanishingOnCollisionComponent implements Component {

    private static final long serialVersionUID = 1L;

    public Duration timeout;
    public Time vanishTime = Time.unset();

    public VanishingOnCollisionComponent(final Duration timeout) {
        this.timeout = timeout;
    }
}
