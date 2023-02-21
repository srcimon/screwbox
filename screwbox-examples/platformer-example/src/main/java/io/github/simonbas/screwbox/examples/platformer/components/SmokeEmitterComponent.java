package io.github.simonbas.screwbox.examples.platformer.components;

import io.github.simonbas.screwbox.core.entities.Component;
import io.github.simonbas.screwbox.core.utils.Timer;

import static io.github.simonbas.screwbox.core.Duration.ofMillis;

public class SmokeEmitterComponent implements Component {

    private static final long serialVersionUID = 1L;

    public final Timer ticker = Timer.withInterval(ofMillis(120));
}
