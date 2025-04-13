package io.github.srcimon.screwbox.platformer.components;

import dev.screwbox.core.environment.Component;

import java.io.Serial;

public class ChangeMapComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public final String fileName;

    public ChangeMapComponent(final String fileName) {
        this.fileName = fileName;
    }
}
