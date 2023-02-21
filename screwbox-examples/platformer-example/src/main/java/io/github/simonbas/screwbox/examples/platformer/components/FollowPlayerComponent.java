package io.github.simonbas.screwbox.examples.platformer.components;

import io.github.simonbas.screwbox.core.entities.Component;

import java.io.Serial;

public class FollowPlayerComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public double speed = 20;
}
