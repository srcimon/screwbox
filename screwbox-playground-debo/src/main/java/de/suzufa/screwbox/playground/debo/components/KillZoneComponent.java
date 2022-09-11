package de.suzufa.screwbox.playground.debo.components;

import de.suzufa.screwbox.core.entities.Component;
import de.suzufa.screwbox.playground.debo.components.DeathEventComponent.DeathType;

public class KillZoneComponent implements Component {

    private static final long serialVersionUID = 1L;

    public final DeathType deathType;

    public KillZoneComponent(final DeathType deathType) {
        this.deathType = deathType;
    }
}
