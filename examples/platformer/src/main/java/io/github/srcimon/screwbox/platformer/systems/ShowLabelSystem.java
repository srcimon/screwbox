package io.github.srcimon.screwbox.platformer.systems;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.assets.FontBundle;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.Order;
import io.github.srcimon.screwbox.core.environment.logic.TriggerAreaComponent;
import io.github.srcimon.screwbox.platformer.components.LabelComponent;

import static io.github.srcimon.screwbox.core.graphics.options.TextDrawOptions.font;

@Order(Order.SystemOrder.PRESENTATION_EFFECTS)
public class ShowLabelSystem implements EntitySystem {

    private static final Archetype LABELD = Archetype.of(TriggerAreaComponent.class, LabelComponent.class);

    @Override
    public void update(Engine engine) {
        for (Entity entity : engine.environment().fetchAll(LABELD)) {
            if (entity.get(TriggerAreaComponent.class).isTriggered) {
                LabelComponent labelComponent = entity.get(LabelComponent.class);
                Bounds bounds = entity.bounds();

                Vector position = Vector.of(bounds.position().x(), bounds.minY());
                engine.graphics().world().drawText(position, labelComponent.label, font(FontBundle.BOLDZILLA).alignCenter().scale(labelComponent.size / 15.0));
            }
        }

    }
}
