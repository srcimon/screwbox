package io.github.srcimon.screwbox.examples.platformer.components;

import io.github.srcimon.screwbox.core.entities.Component;

import java.io.Serial;

public class WaypointComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public final int next;

    public WaypointComponent(final int next) {
        this.next = next;
    }
}
