package io.github.srcimon.screwbox.examples.platformer.components;

import io.github.srcimon.screwbox.core.environment.Component;

import java.io.Serial;

public class PatrollingMovementComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public boolean right = true;
}
