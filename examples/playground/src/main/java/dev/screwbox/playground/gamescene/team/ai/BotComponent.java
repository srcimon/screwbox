package dev.screwbox.playground.gamescene.team.ai;

import dev.screwbox.core.environment.Component;
import dev.screwbox.core.utils.Scheduler;

public class BotComponent implements Component {

    public enum State {
        FETCH_FLAG
    }

    public Scheduler scheduler = Scheduler.ofSeconds(1);//TODO randomizeOffset

    public State state;
}
