package de.suzufa.screwbox.examples.helloworld.systems;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.entityengine.EntitySystem;
import de.suzufa.screwbox.core.keyboard.Key;

public class QuitOnKeystrokeSystem implements EntitySystem {

    private final Key quitKey;

    public QuitOnKeystrokeSystem(Key quitKey) {
        this.quitKey = quitKey;
    }

    @Override
    public void update(Engine engine) {
        if (engine.keyboard().justPressed(quitKey)) {
            engine.stop();
        }

    }

}
