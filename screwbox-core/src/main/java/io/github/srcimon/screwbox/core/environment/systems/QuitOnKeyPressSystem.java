package io.github.srcimon.screwbox.core.environment.systems;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
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
