package de.suzufa.screwbox.playground.debo.components;

import de.suzufa.screwbox.core.entities.Component;

public class DeathEventComponent implements Component {

    private static final long serialVersionUID = 1L;

    public enum DeathType {
        SPIKES,
        LAVA,
        ENEMY_TOUCHED,
        WATER;
    }

    public final DeathType deathType;

    public DeathEventComponent() {
        this(DeathType.ENEMY_TOUCHED);
    }

    public DeathEventComponent(final DeathType deathType) {
        this.deathType = deathType;
    }

}
