package dev.screwbox.platformer.systems;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Engine;
import dev.screwbox.core.Vector;
import dev.screwbox.core.assets.FontBundle;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.ExecutionOrder;
import dev.screwbox.core.environment.Order;
import dev.screwbox.core.environment.logic.TriggerAreaComponent;
import dev.screwbox.platformer.components.LabelComponent;

import static dev.screwbox.core.graphics.options.TextDrawOptions.font;

@ExecutionOrder(Order.PRESENTATION_EFFECTS)
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
