package dev.screwbox.playground.gamescene.teams.health;

import dev.screwbox.core.Percent;
import dev.screwbox.core.environment.Component;

public class HealthComponent implements Component {

    public Percent health = Percent.max();
}
