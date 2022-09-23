package de.suzufa.screwbox.examples.gameoflife.systems;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.entities.EntitySystem;
import de.suzufa.screwbox.core.keyboard.Key;

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
