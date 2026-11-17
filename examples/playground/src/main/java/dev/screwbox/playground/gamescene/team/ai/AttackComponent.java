package dev.screwbox.playground.gamescene.team.ai;

import dev.screwbox.core.environment.Component;
import dev.screwbox.core.utils.Scheduler;

public class AttackComponent implements Component {

    public double range = 50;
    public Scheduler scheduler = Scheduler.ofSeconds(2);
}
