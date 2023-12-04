package io.github.srcimon.screwbox.examples.platformer.components;

import io.github.srcimon.screwbox.core.Time;
import io.github.srcimon.screwbox.core.environment.Component;

public class ChangeMapComponent implements Component {

    private static final long serialVersionUID = 1L;

    public final String fileName;
    public Time time = Time.unset();

    public ChangeMapComponent(final String fileName) {
        this.fileName = fileName;
    }
}
