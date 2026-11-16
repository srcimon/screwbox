package dev.screwbox.playground.gamescene.team.ai;

import dev.screwbox.core.Duration;
import dev.screwbox.core.environment.Component;
import dev.screwbox.core.utils.Scheduler;

public class BotComponent implements Component {

    public enum State {
        FETCH_FLAG
    }

    public Scheduler scheduler = Scheduler.withInterval(Duration.ofSeconds(1));//TODO Scheduler.seconds(2) & milliseconds()

    public State state;
}
