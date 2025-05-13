package dev.screwbox.playground;

import dev.screwbox.core.Duration;
import dev.screwbox.core.environment.Component;
import dev.screwbox.core.utils.Scheduler;

public class SplashComponent implements Component {

    public double threshold = 10;
    public Scheduler scheduler = Scheduler.withInterval(Duration.ofMillis(20));
}
