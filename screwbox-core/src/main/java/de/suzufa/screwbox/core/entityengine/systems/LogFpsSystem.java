package de.suzufa.screwbox.core.entityengine.systems;

import static de.suzufa.screwbox.core.Duration.ofSeconds;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.Timer;
import de.suzufa.screwbox.core.entityengine.EntitySystem;
import de.suzufa.screwbox.core.loop.Metrics;
import de.suzufa.screwbox.core.util.MeanValue;

public class LogFpsSystem implements EntitySystem {

    private final MeanValue fpsMean = new MeanValue();
    private final Timer timer = Timer.withIntervalOf(ofSeconds(2));

    @Override
    public void update(final Engine engine) {
        Metrics metrics = engine.loop().metrics();
        fpsMean.addValue(metrics.framesPerSecond());

        if (timer.isNow(metrics.timeOfLastUpdate())) {
            String fpsMessage = String.format("FPS %.0f", fpsMean.calculate());
            engine.log().debug(fpsMessage);
            fpsMean.reset();
        }
    }

}
