package de.suzufa.screwbox.playground.debo.components;

import de.suzufa.screwbox.core.entityengine.Component;

public class WaypointComponent implements Component {

    private static final long serialVersionUID = 1L;

    public final int next;

    public WaypointComponent(final int next) {
        this.next = next;
    }
}
