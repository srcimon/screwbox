package io.github.srcimon.screwbox.platformer.components;

import dev.screwbox.core.environment.Component;

import java.io.Serial;

public class WaypointComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public final int next;

    public WaypointComponent(final int next) {
        this.next = next;
    }
}
