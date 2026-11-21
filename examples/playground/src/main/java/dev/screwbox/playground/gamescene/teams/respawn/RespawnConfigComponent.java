package dev.screwbox.playground.gamescene.teams.respawn;

import dev.screwbox.core.Duration;
import dev.screwbox.core.environment.Component;

public class RespawnConfigComponent implements Component {

    public Duration respawnTimeout = Duration.ofSeconds(5);
}
