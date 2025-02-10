package io.github.srcimon.screwbox.vacuum.enemies;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.environment.Component;
import io.github.srcimon.screwbox.core.utils.Scheduler;

public class RunAtPlayerComponent implements Component {

    public Scheduler refreshPathScheduler = Scheduler.withInterval(Duration.ofMillis(250));
}
