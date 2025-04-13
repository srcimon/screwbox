package dev.screwbox.platformer.components;

import dev.screwbox.core.environment.Component;

public class KillZoneComponent implements Component {

    private static final long serialVersionUID = 1L;

    public final DeathEventComponent.DeathType deathType;

    public KillZoneComponent(final DeathEventComponent.DeathType deathType) {
        this.deathType = deathType;
    }
}
