package de.suzufa.screwbox.examples.light;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.entities.EntitySystem;
import de.suzufa.screwbox.core.entities.UpdatePriority;
import de.suzufa.screwbox.core.graphics.Color;

public class BlueBackgroundSystem implements EntitySystem {

    @Override
    public void update(Engine engine) {
        engine.graphics().window().fillWith(Color.BLUE);
    }

    @Override
    public UpdatePriority updatePriority() {
        return UpdatePriority.PRESENTATION_WORLD;
    }
}
