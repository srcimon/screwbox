package io.github.srcimon.screwbox.examples.platformer.systems;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.entities.*;
import io.github.srcimon.screwbox.core.entities.components.SignalComponent;
import io.github.srcimon.screwbox.core.entities.components.TransformComponent;
import io.github.srcimon.screwbox.examples.platformer.components.LabelComponent;

import static io.github.srcimon.screwbox.core.graphics.Color.WHITE;
import static io.github.srcimon.screwbox.core.graphics.Pixelfont.defaultFont;

@Order(SystemOrder.PRESENTATION_EFFECTS)
public class ShowLabelSystem implements EntitySystem {

    private static final Archetype LABELD = Archetype.of(SignalComponent.class, LabelComponent.class);

    @Override
    public void update(Engine engine) {
        for (Entity entity : engine.entities().fetchAll(LABELD)) {
            if (entity.get(SignalComponent.class).isTriggered) {
                LabelComponent labelComponent = entity.get(LabelComponent.class);
                Bounds bounds = entity.get(TransformComponent.class).bounds;

                Vector position = Vector.of(bounds.position().x(), bounds.minY());
                engine.graphics().world().drawTextCentered(position, labelComponent.label, defaultFont(WHITE),
                        Percent.max(),
                        labelComponent.size / 15.0);
            }
        }

    }
}
