package dev.screwbox.playground.gamescene.team;

import dev.screwbox.core.Time;
import dev.screwbox.core.environment.Component;
import dev.screwbox.core.graphics.Color;

public class TeamMemberComponent implements Component {

    public String name;
    public Color team;
    public Time deathTime = Time.unset();
}
