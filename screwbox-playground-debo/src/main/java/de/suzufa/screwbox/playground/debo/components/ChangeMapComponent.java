package de.suzufa.screwbox.playground.debo.components;

import de.suzufa.screwbox.core.Time;
import de.suzufa.screwbox.core.entityengine.Component;

public class ChangeMapComponent implements Component {

    private static final long serialVersionUID = 1L;

    public final String fileName;
    public Time time = Time.unset();

    public ChangeMapComponent(final String fileName) {
        this.fileName = fileName;
    }
}
