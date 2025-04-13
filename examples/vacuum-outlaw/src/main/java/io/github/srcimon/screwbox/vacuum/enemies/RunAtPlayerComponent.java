package io.github.srcimon.screwbox.vacuum.enemies;

import dev.screwbox.core.Duration;
import dev.screwbox.core.environment.Component;
import dev.screwbox.core.utils.Scheduler;

public class RunAtPlayerComponent implements Component {

    public Scheduler refreshPathScheduler = Scheduler.withInterval(Duration.ofMillis(250));
}
