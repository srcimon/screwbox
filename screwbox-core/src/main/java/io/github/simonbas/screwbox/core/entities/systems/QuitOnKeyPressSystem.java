package io.github.simonbas.screwbox.core.entities.systems;

import io.github.simonbas.screwbox.core.Engine;
import io.github.simonbas.screwbox.core.entities.EntitySystem;
import io.github.simonbas.screwbox.core.keyboard.Key;

public class QuitOnKeyPressSystem implements EntitySystem {

    private final Key quitKey;

    public QuitOnKeyPressSystem(final Key quitKey) {
        this.quitKey = quitKey;
    }

    @Override
    public void update(final Engine engine) {
        if (engine.keyboard().justPressed(quitKey)) {
            engine.stop();
        }
    }

}
