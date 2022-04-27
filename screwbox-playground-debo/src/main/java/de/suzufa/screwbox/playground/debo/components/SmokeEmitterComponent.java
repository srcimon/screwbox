package de.suzufa.screwbox.playground.debo.components;

import de.suzufa.screwbox.core.Time;
import de.suzufa.screwbox.core.entityengine.Component;

public class SmokeEmitterComponent implements Component {

    private static final long serialVersionUID = 1L;

    public Time lastEmitted = Time.now();
}
