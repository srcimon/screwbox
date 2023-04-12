package io.github.srcimon.screwbox.core.entities.systems;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.entities.EntitySystem;
import io.github.srcimon.screwbox.core.utils.Timer;
import io.github.srcimon.screwbox.core.Duration;

public class LogFpsSystem implements EntitySystem {

    private final Timer timer = Timer.withInterval(Duration.ofSeconds(2));

    private double sum;
    private long count;

    @Override
    public void update(final Engine engine) {
        sum += engine.loop().fps();
        count++;
        if (timer.isTick(engine.loop().lastUpdate())) {
            double average = sum / count;
            sum = 0;
            count = 0;
            final String fpsMessage = String.format("current fps %,.0f", average);
            engine.log().debug(fpsMessage);
        }
    }

}
