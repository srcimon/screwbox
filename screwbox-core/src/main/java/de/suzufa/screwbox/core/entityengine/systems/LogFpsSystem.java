package de.suzufa.screwbox.core.entityengine.systems;

import static de.suzufa.screwbox.core.Duration.ofSeconds;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.entityengine.EntitySystem;
import de.suzufa.screwbox.core.loop.Metrics;
import de.suzufa.screwbox.core.utils.Timer;

public class LogFpsSystem implements EntitySystem {

    private final Timer timer = Timer.withInterval(ofSeconds(2));

    private double sum;
    private long count;

    @Override
    public void update(final Engine engine) {
        Metrics metrics = engine.loop().metrics();
        sum += metrics.framesPerSecond();
        count++;
        if (timer.isTick(metrics.lastUpdate())) {
            String fpsMessage = String.format("current fps %,.0f", sum / count);
            engine.log().debug(fpsMessage);
            sum = 0;
            count = 0;
        }
    }

}
