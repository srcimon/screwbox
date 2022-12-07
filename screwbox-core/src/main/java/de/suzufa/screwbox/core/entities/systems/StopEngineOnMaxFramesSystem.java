package de.suzufa.screwbox.core.entities.systems;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.entities.EntitySystem;

public class StopEngineOnMaxFramesSystem implements EntitySystem {

    private final int maxFrames;

    public StopEngineOnMaxFramesSystem(final int maxFrames) {
        this.maxFrames = maxFrames;
    }

    @Override
    public void update(final Engine engine) {
        if (engine.loop().frameNumber() >= maxFrames) {
            engine.stop();
        }
    }

}
