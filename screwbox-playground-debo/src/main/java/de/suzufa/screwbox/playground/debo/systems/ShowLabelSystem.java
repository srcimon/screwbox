package de.suzufa.screwbox.playground.debo.systems;

import static de.suzufa.screwbox.core.graphics.world.WorldText.centeredText;

import de.suzufa.screwbox.core.Bounds;
import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.Vector;
import de.suzufa.screwbox.core.entityengine.Archetype;
import de.suzufa.screwbox.core.entityengine.Entity;
import de.suzufa.screwbox.core.entityengine.EntitySystem;
import de.suzufa.screwbox.core.entityengine.UpdatePriority;
import de.suzufa.screwbox.core.entityengine.components.TransformComponent;
import de.suzufa.screwbox.core.entityengine.components.SignalComponent;
import de.suzufa.screwbox.core.graphics.Font;
import de.suzufa.screwbox.playground.debo.components.LabelComponent;

public class ShowLabelSystem implements EntitySystem {

    private static final Archetype LABELD = Archetype.of(SignalComponent.class, LabelComponent.class);

    @Override
    public void update(Engine engine) {
        for (Entity entity : engine.entityEngine().fetchAll(LABELD)) {
            if (entity.get(SignalComponent.class).isTriggered) {
                LabelComponent labelComponent = entity.get(LabelComponent.class);
                Font font = new Font("Futura", labelComponent.size);
                Bounds bounds = entity.get(TransformComponent.class).bounds;

                Vector position = Vector.of(bounds.position().x(), bounds.minY());
                engine.graphics().world().draw(centeredText(position, labelComponent.label, font));
            }
        }

    }

    @Override
    public UpdatePriority updatePriority() {
        return UpdatePriority.PRESENTATION_EFFECTS;
    }

}
