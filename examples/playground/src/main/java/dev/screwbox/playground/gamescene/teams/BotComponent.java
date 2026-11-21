package dev.screwbox.playground.gamescene.teams;

import dev.screwbox.core.environment.Component;
import dev.screwbox.core.utils.Scheduler;

public class BotComponent implements Component {

    public Scheduler scheduler = Scheduler.ofRandomMillis(600, 1400);

}
