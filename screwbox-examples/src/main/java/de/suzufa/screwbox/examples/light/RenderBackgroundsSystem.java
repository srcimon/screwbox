package de.suzufa.screwbox.examples.light;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.entityengine.EntitySystem;
import de.suzufa.screwbox.core.graphics.Color;

public class RenderBackgroundsSystem implements EntitySystem {

    @Override
    public void update(Engine engine) {
        engine.graphics().window().fillWith(Color.DARK_BLUE);
    }

}
