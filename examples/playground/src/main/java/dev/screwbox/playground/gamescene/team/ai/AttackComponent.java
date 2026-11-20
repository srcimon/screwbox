package dev.screwbox.playground.gamescene.team.ai;

import dev.screwbox.core.environment.Component;
import dev.screwbox.core.utils.Scheduler;

public class AttackComponent implements Component {

    public double range = 140;
    public Scheduler scheduler = Scheduler.ofMillis(500);
}
