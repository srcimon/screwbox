package dev.screwbox.playground;

import dev.screwbox.core.Ease;
import dev.screwbox.core.Engine;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.EntityListener;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.light.BackdropOccluderComponent;
import dev.screwbox.core.environment.physics.AttachmentComponent;
import dev.screwbox.core.environment.rendering.RenderComponent;
import dev.screwbox.core.keyboard.Key;

public class JumpSystem implements EntitySystem {
    @Override
    public void update(Engine engine) {
        if(engine.keyboard().isPressed(Key.SPACE)) {
            engine.environment().fetchById(1).add(new JumpComponent());
        }
        for(var e : engine.environment().fetchAllHaving(JumpComponent.class)){
            var jump = e.get(JumpComponent.class);
            jump.progress = jump.progress.add(engine.loop().delta(2));
            var render = engine.environment().fetchById(2).get(AttachmentComponent.class);
            render.offset = Vector.y(Ease.SINE_IN_OUT.applyOn(jump.progress).rangeValue(0, -22));
            e.get(BackdropOccluderComponent.class).options = e.get(BackdropOccluderComponent.class).options.offset(render.offset);
            if( jump.progress.isMax()) {
                e.remove(JumpComponent.class);
            }

        }
    }
}
