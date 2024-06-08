package io.github.srcimon.screwbox.platformer.components;

import io.github.srcimon.screwbox.core.environment.Component;

import java.io.Serial;

public class GroundDetectorComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public boolean isOnGround = false;
}
