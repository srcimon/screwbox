package dev.screwbox.platformer.systems;

import dev.screwbox.core.Engine;
import dev.screwbox.core.Vector;
import dev.screwbox.core.assets.FontBundle;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.ExecutionOrder;
import dev.screwbox.core.environment.Order;
import dev.screwbox.core.environment.logic.TriggerAreaComponent;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.options.TextDrawOptions;
import dev.screwbox.platformer.components.LabelComponent;

import static dev.screwbox.core.graphics.options.TextDrawOptions.font;

@ExecutionOrder(Order.PRESENTATION_EFFECTS)
public class ShowLabelSystem implements EntitySystem {

    private static final Archetype LABELED = Archetype.of(TriggerAreaComponent.class, LabelComponent.class);
    private static final TextDrawOptions OPTIONS = font(FontBundle.BOLDZILLA).alignCenter()
        .highlightFont(1, FontBundle.BOLDZILLA.customColor(Color.YELLOW));

    @Override
    public void update(Engine engine) {
        for (final var entity : engine.environment().fetchAll(LABELED)) {
            if (entity.get(TriggerAreaComponent.class).isTriggered) {
                LabelComponent labelComponent = entity.get(LabelComponent.class);
                Vector position = Vector.of(entity.position().x(), entity.bounds().minY());
                engine.graphics().world().drawText(position, labelComponent.label, OPTIONS.scale(labelComponent.size / 15.0));
            }
        }

    }
}
