package dev.screwbox.playground.gamescene.spawn;

import dev.screwbox.core.Angle;
import dev.screwbox.core.Duration;
import dev.screwbox.core.environment.Component;
import dev.screwbox.core.graphics.Color;

import java.util.Set;

public class RespawnConfigComponent implements Component {

    public Set<Color> teams = Set.of(Color.RED, Color.BLUE);
    public int teamSize = 4;
    public Duration respawnTimeout = Duration.ofSeconds(2);
}
