package de.suzufa.screwbox.core.entityengine.systems;

import static de.suzufa.screwbox.core.Duration.ofSeconds;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.entityengine.EntitySystem;
import de.suzufa.screwbox.core.utils.Timer;

public class LogFpsSystem implements EntitySystem {

    private final Timer timer = Timer.withInterval(ofSeconds(2));

    private double sum;
    private long count;

    @Override
    public void update(final Engine engine) {
        sum += engine.loop().fps();
        count++;
//TODO: what am i doing right here?
        if (timer.isTick(engine.loop().lastUpdate())) {
            String fpsMessage = String.format("current fps %,.0f", sum / count);
            engine.log().debug(fpsMessage);
            sum = 0;
            count = 0;
        }
    }

}
