package de.suzufa.screwbox.core.entities.systems;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.entities.EntitySystem;
import de.suzufa.screwbox.core.keyboard.Key;

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
