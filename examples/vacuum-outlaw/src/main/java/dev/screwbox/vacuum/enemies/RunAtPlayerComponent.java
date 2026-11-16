package dev.screwbox.vacuum.enemies;

import dev.screwbox.core.Duration;
import dev.screwbox.core.environment.Component;
import dev.screwbox.core.utils.Scheduler;

public class RunAtPlayerComponent implements Component {

    public Scheduler refreshPathScheduler = Scheduler.ofInterval(Duration.ofMillis(250));
}
