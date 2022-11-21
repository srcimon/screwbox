package de.suzufa.screwbox.playground.debo.systems;

import static de.suzufa.screwbox.core.graphics.Color.WHITE;
import static de.suzufa.screwbox.core.graphics.Pixelfont.defaultFont;

import de.suzufa.screwbox.core.Bounds;
import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.Percent;
import de.suzufa.screwbox.core.Vector;
import de.suzufa.screwbox.core.entities.Archetype;
import de.suzufa.screwbox.core.entities.Entity;
import de.suzufa.screwbox.core.entities.EntitySystem;
import de.suzufa.screwbox.core.entities.UpdatePriority;
import de.suzufa.screwbox.core.entities.components.SignalComponent;
import de.suzufa.screwbox.core.entities.components.TransformComponent;
import de.suzufa.screwbox.playground.debo.components.LabelComponent;

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

    @Override
    public UpdatePriority updatePriority() {
        return UpdatePriority.PRESENTATION_EFFECTS;
    }

}
