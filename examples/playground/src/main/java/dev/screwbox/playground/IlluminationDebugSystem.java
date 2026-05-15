package dev.screwbox.playground;

import dev.screwbox.core.Engine;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.keyboard.Key;

public class IlluminationDebugSystem implements EntitySystem {

    @Override
    public void update(final Engine engine) {
        engine.graphics().camera().changeZoomBy(engine.mouse().unitsScrolled() / 20.0);
        for (var entity : engine.environment().fetchAllHaving(IlluminationComponent.class)) {
            if (!engine.keyboard().isDown(Key.SPACE)) {
                engine.graphics().light().addIllumination(entity.position(), entity.get(IlluminationComponent.class).radius*0.8, Color.BLACK);
            }
        }
    }
}
