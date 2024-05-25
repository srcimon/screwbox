package io.github.srcimon.screwbox.platformer.components;

import io.github.srcimon.screwbox.core.environment.Component;

import java.io.Serial;

public class DeathEventComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public enum DeathType {
        SPIKES,
        LAVA,
        ENEMY_TOUCHED,
        WATER
    }

    public final DeathType deathType;

    public DeathEventComponent() {
        this(DeathType.ENEMY_TOUCHED);
    }

    public DeathEventComponent(final DeathType deathType) {
        this.deathType = deathType;
    }

}
