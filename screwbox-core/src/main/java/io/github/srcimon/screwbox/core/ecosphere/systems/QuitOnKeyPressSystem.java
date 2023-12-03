package io.github.srcimon.screwbox.core.ecosphere.systems;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.ecosphere.EntitySystem;
import io.github.srcimon.screwbox.core.keyboard.Key;

public class QuitOnKeyPressSystem implements EntitySystem {

    private final Key quitKey;

    public QuitOnKeyPressSystem() {
        this(Key.ESCAPE);
    }

    public QuitOnKeyPressSystem(final Key quitKey) {
        this.quitKey = quitKey;
    }

    @Override
    public void update(final Engine engine) {
        if (engine.keyboard().isPressed(quitKey)) {
            engine.stop();
        }
    }

}
