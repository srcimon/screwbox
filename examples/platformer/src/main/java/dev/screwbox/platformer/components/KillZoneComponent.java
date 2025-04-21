package dev.screwbox.platformer.components;

import dev.screwbox.core.environment.Component;

import java.io.Serial;

public class KillZoneComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public final DeathEventComponent.DeathType deathType;

    public KillZoneComponent(final DeathEventComponent.DeathType deathType) {
        this.deathType = deathType;
    }
}
