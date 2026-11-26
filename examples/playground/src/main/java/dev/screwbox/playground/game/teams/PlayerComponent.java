package dev.screwbox.playground.game.teams;

import dev.screwbox.core.Time;
import dev.screwbox.core.environment.Component;

public class PlayerComponent implements Component {

    public String name;
    public int teamId;
    public Time deathTime = Time.unset();
}
