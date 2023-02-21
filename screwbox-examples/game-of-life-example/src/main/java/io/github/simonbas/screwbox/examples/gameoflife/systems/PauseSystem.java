package io.github.simonbas.screwbox.examples.gameoflife.systems;

import io.github.simonbas.screwbox.core.Engine;
import io.github.simonbas.screwbox.core.entities.EntitySystem;
import io.github.simonbas.screwbox.core.keyboard.Key;

public class PauseSystem implements EntitySystem {

    private final Key pauseKey;

    public PauseSystem(final Key pauseKey) {
        this.pauseKey = pauseKey;
    }

    @Override
    public void update(final Engine engine) {
        var entities = engine.entities();
        if (engine.keyboard().justPressed(pauseKey)) {
            if (entities.isSystemPresent(GridUpdateSystem.class)) {
                entities.remove(GridUpdateSystem.class);
            } else {
                entities.add(new GridUpdateSystem());
            }
        }
    }

}
