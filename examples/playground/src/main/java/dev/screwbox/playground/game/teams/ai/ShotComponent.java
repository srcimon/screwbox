package dev.screwbox.playground.game.teams.ai;

import dev.screwbox.core.Percent;
import dev.screwbox.core.environment.Component;

public class ShotComponent implements Component {

    public int teamId;
    public Percent damage = Percent.of(0.25);
}
