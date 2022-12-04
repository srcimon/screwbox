package de.suzufa.screwbox.examples.platformer.components;

import de.suzufa.screwbox.core.Duration;
import de.suzufa.screwbox.core.Time;
import de.suzufa.screwbox.core.entities.Component;

public class VanishingOnCollisionComponent implements Component {

    private static final long serialVersionUID = 1L;

    public Duration timeout;
    public Time vanishTime = Time.unset();

    public VanishingOnCollisionComponent(final Duration timeout) {
        this.timeout = timeout;
    }
}
