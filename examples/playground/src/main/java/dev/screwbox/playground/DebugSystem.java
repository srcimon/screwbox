package dev.screwbox.playground;

import dev.screwbox.core.Angle;
import dev.screwbox.core.Engine;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.light.ConeLightComponent;
import dev.screwbox.core.environment.physics.AttachmentComponent;
import dev.screwbox.core.environment.softphysics.SoftLinkComponent;

public class DebugSystem implements EntitySystem {

    @Override
    public void update(final Engine engine) {
        for (var cone : engine.environment().fetchAllHaving(ConeLightComponent.class)) {

            Angle dir = engine.environment().fetchById(cone.get(AttachmentComponent.class).targetId).get(SoftLinkComponent.class).angle.addDegrees(180);
            cone.get(ConeLightComponent.class).direction = Angle.degrees(Math.clamp(dir.degrees(), 90, 270));
        }
    }
}
