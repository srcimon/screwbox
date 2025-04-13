package dev.screwbox.core.environment.core;

import dev.screwbox.core.Engine;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.keyboard.Key;

public class QuitOnKeySystem implements EntitySystem {

    private final Key quitKey;

    public QuitOnKeySystem() {
        this(Key.ESCAPE);
    }

    public QuitOnKeySystem(final Key quitKey) {
        this.quitKey = quitKey;
    }

    @Override
    public void update(final Engine engine) {
        if (engine.keyboard().isPressed(quitKey)) {
            engine.stop();
        }
    }

}
