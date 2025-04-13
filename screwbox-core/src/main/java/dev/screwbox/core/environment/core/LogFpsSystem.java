package dev.screwbox.core.environment.core;

import dev.screwbox.core.Duration;
import dev.screwbox.core.Engine;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.utils.Scheduler;

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
