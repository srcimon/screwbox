package de.suzufa.screwbox.core.entityengine.systems;

import static de.suzufa.screwbox.core.Duration.ofSeconds;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.Timer;
import de.suzufa.screwbox.core.entityengine.EntitySystem;
import de.suzufa.screwbox.core.loop.Metrics;

public class LogFpsSystem implements EntitySystem {

    private final Timer timer = Timer.withIntervalOf(ofSeconds(2));

    private double sum;
    private long count;

    @Override
    public void update(final Engine engine) {
        Metrics metrics = engine.loop().metrics();
        sum += metrics.framesPerSecond();
        count++;
        if (timer.isTick(metrics.timeOfLastUpdate())) {
            String fpsMessage = String.format("current fps %.0f", sum / count);
            engine.log().debug(fpsMessage);
            sum = 0;
            count = 0;
        }
    }

}
