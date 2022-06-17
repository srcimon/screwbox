package de.suzufa.screwbox.playground.debo.components;

import static de.suzufa.screwbox.core.Duration.ofMillis;

import de.suzufa.screwbox.core.entityengine.Component;
import de.suzufa.screwbox.core.utils.Timer;

public class SmokeEmitterComponent implements Component {

    private static final long serialVersionUID = 1L;

    public final Timer ticker = Timer.withIntervalOf(ofMillis(120));
}
