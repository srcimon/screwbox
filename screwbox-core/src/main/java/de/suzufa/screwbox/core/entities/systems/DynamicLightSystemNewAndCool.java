package de.suzufa.screwbox.core.entities.systems;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.entities.EntitySystem;
import de.suzufa.screwbox.core.entities.UpdatePriority;

public class DynamicLightSystemNewAndCool implements EntitySystem {

    @Override
    public void update(Engine engine) {
        engine.graphics().light().drawLightmap();
    }

    @Override
    public UpdatePriority updatePriority() {
        return UpdatePriority.PRESENTATION_LIGHT;
    }
}