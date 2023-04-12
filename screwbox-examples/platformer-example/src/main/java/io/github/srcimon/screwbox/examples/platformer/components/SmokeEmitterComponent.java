package io.github.srcimon.screwbox.examples.platformer.components;

import io.github.srcimon.screwbox.core.entities.Component;
import io.github.srcimon.screwbox.core.utils.Timer;

import static io.github.srcimon.screwbox.core.Duration.ofMillis;

public class SmokeEmitterComponent implements Component {

    private static final long serialVersionUID = 1L;

    public final Timer ticker = Timer.withInterval(ofMillis(120));
}
