package dev.screwbox.playground;

import dev.screwbox.core.Angle;
import dev.screwbox.core.Engine;
import dev.screwbox.core.Percent;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.light.ConeLightComponent;

public class DebugSystem implements EntitySystem {

    @Override
    public void update(final Engine engine) {
        for (var cone : engine.environment().fetchAllHaving(ConeLightComponent.class)) {
            cone.get(ConeLightComponent.class).direction = Angle.degrees(cone.get(ConeLightComponent.class).direction.degrees() + engine.mouse().unitsScrolled());
        }
        if (engine.mouse().isPressedLeft()) {
            engine.graphics().configuration().setIndirectLightBounceLossFactor(engine.graphics().configuration().indirectLightBounceLossFactor().step(0.1));
            System.out.println(engine.graphics().configuration().indirectLightBounceLossFactor().value());
        }
    }
}
