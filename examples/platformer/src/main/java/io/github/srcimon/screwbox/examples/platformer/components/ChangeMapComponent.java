package io.github.srcimon.screwbox.examples.platformer.components;

import io.github.srcimon.screwbox.core.environment.Component;

import java.io.Serial;

public class ChangeMapComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public final String fileName;

    public ChangeMapComponent(final String fileName) {
        this.fileName = fileName;
    }
}
