package io.github.simonbas.screwbox.examples.platformer.components;

import io.github.simonbas.screwbox.core.entities.Component;

import java.io.Serial;

public class MovableComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public double maxSpeed = 30;
}
