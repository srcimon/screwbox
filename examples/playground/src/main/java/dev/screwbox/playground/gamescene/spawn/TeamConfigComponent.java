package dev.screwbox.playground.gamescene.spawn;

import dev.screwbox.core.Time;
import dev.screwbox.core.environment.Component;
import dev.screwbox.core.graphics.Color;

public class TeamConfigComponent implements Component {

    public Color teamColor;
    public int teamSize;
    public Time lastRespawn = Time.unset();
}
