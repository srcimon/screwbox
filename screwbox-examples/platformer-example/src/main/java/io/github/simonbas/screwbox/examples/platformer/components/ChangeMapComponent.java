package io.github.simonbas.screwbox.examples.platformer.components;

import io.github.simonbas.screwbox.core.Time;
import io.github.simonbas.screwbox.core.entities.Component;

public class ChangeMapComponent implements Component {

    private static final long serialVersionUID = 1L;

    public final String fileName;
    public Time time = Time.unset();

    public ChangeMapComponent(final String fileName) {
        this.fileName = fileName;
    }
}
