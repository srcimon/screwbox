package io.github.simonbas.screwbox.examples.platformer.components;

import io.github.simonbas.screwbox.core.entities.Component;
import io.github.simonbas.screwbox.examples.platformer.components.DeathEventComponent.DeathType;

public class KillZoneComponent implements Component {

    private static final long serialVersionUID = 1L;

    public final DeathType deathType;

    public KillZoneComponent(final DeathType deathType) {
        this.deathType = deathType;
    }
}
