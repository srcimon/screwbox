package io.github.srcimon.screwbox.platformer.components;

import io.github.srcimon.screwbox.core.environment.Component;

import java.io.Serial;

public class FollowPlayerComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public double speed = 20;
}
