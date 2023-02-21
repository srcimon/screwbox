package io.github.simonbas.screwbox.examples.platformer.components;

import io.github.simonbas.screwbox.core.entities.Component;

import java.io.Serial;

public final class CurrentLevelComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public final String name;

    public CurrentLevelComponent(final String name) {
        this.name = name;
    }
}
