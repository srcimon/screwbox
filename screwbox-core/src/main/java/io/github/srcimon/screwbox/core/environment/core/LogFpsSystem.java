package io.github.srcimon.screwbox.core.environment.core;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.utils.Scheduler;

public class LogFpsSystem implements EntitySystem {

    private final Scheduler scheduler = Scheduler.withInterval(Duration.ofSeconds(2));

    private double sum;
    private long count;

    @Override
    public void update(final Engine engine) {
        sum += engine.loop().fps();
        count++;
        if (scheduler.isTick(engine.loop().time())) {
            double average = sum / count;
            sum = 0;
            count = 0;
            final String fpsMessage = String.format("current fps %,.0f", average);
            engine.log().debug(fpsMessage);
        }
    }

}
