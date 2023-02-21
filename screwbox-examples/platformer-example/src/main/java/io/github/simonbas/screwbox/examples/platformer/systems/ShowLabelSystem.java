package io.github.simonbas.screwbox.examples.platformer.systems;

import io.github.simonbas.screwbox.core.Bounds;
import io.github.simonbas.screwbox.core.Engine;
import io.github.simonbas.screwbox.core.Percent;
import io.github.simonbas.screwbox.core.Vector;
import io.github.simonbas.screwbox.core.entities.*;
import io.github.simonbas.screwbox.core.entities.components.SignalComponent;
import io.github.simonbas.screwbox.core.entities.components.TransformComponent;
import io.github.simonbas.screwbox.examples.platformer.components.LabelComponent;

import static io.github.simonbas.screwbox.core.graphics.Color.WHITE;
import static io.github.simonbas.screwbox.core.graphics.Pixelfont.defaultFont;

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
