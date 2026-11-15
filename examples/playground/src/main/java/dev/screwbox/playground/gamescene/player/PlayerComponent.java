package dev.screwbox.playground.gamescene.player;

import dev.screwbox.core.Time;
import dev.screwbox.core.environment.Component;
import dev.screwbox.core.graphics.Color;

public class PlayerComponent implements Component {

    public String name;
    public Color team;
    public boolean isDead = true;
    public Time lastRespawn = Time.unset();
}
