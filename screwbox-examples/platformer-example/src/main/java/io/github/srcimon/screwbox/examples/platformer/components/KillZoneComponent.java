package io.github.srcimon.screwbox.examples.platformer.components;

import io.github.srcimon.screwbox.core.entities.Component;

public class KillZoneComponent implements Component {

    private static final long serialVersionUID = 1L;

    public final DeathEventComponent.DeathType deathType;

    public KillZoneComponent(final DeathEventComponent.DeathType deathType) {
        this.deathType = deathType;
    }
}
