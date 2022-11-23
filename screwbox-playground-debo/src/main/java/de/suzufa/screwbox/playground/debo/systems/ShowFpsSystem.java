package de.suzufa.screwbox.playground.debo.systems;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.entities.Archetype;
import de.suzufa.screwbox.core.entities.EntitySystem;
import de.suzufa.screwbox.core.entities.UpdatePriority;
import de.suzufa.screwbox.core.entities.components.ColliderComponent;
import de.suzufa.screwbox.core.entities.components.TransformComponent;
import de.suzufa.screwbox.core.graphics.Color;
import de.suzufa.screwbox.core.graphics.Font;
import de.suzufa.screwbox.core.graphics.Offset;

public class ShowFpsSystem implements EntitySystem {

    private static final Archetype COLLIDERS = Archetype.of(ColliderComponent.class, TransformComponent.class);
    private static final Offset TEXT_POSITION = Offset.at(50, 50);
    private static final Font FONT = new Font("Futura", 14);

    @Override
    public void update(Engine engine) {
        long colliderCount = engine.entities().fetchAll(COLLIDERS).size();
        long entityCount = engine.entities().entityCount();
        int fps = engine.loop().fps();
        long updateTime = engine.loop().updateDuration().milliseconds();
        String text = String.format("fps: %d / updatetime %02d / %d entities / %d colliders",
                fps, updateTime, entityCount, colliderCount);
        engine.graphics().screen().drawText(TEXT_POSITION, text, FONT, Color.WHITE);
    }

    @Override
    public UpdatePriority updatePriority() {
        return UpdatePriority.PRESENTATION_UI_FOREGROUND;
    }
}
