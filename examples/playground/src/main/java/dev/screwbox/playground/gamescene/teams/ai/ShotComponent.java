package dev.screwbox.playground.gamescene.teams.ai;

import dev.screwbox.core.Percent;
import dev.screwbox.core.environment.Component;
import dev.screwbox.core.graphics.Color;

public class ShotComponent implements Component {

    public Color team;
    public Percent damage = Percent.of(0.25);
}
