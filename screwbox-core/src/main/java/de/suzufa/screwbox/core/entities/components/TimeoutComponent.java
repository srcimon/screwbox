package de.suzufa.screwbox.core.entities.components;

import de.suzufa.screwbox.core.Time;
import de.suzufa.screwbox.core.entities.Component;

public class TimeoutComponent implements Component {

    private static final long serialVersionUID = 1L;

    public final Time timeout;

    public TimeoutComponent(final Time timeout) {
        this.timeout = timeout;
    }
}
