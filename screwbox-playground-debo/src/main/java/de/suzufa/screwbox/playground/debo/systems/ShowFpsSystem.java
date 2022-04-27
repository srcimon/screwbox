package de.suzufa.screwbox.playground.debo.systems;

import static de.suzufa.screwbox.core.graphics.window.WindowText.text;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.entityengine.Archetype;
import de.suzufa.screwbox.core.entityengine.EntitySystem;
import de.suzufa.screwbox.core.entityengine.UpdatePriority;
import de.suzufa.screwbox.core.entityengine.components.TransformComponent;
import de.suzufa.screwbox.core.entityengine.components.ColliderComponent;
import de.suzufa.screwbox.core.graphics.Font;
import de.suzufa.screwbox.core.graphics.Offset;

public class ShowFpsSystem implements EntitySystem {

    private static final Archetype COLLIDERS = Archetype.of(ColliderComponent.class, TransformComponent.class);
    private static final Offset TEXT_POSITION = Offset.at(50, 50);
    private static final Font FONT = new Font("Futura", 14);

    @Override
    public void update(Engine engine) {
        long colliderCount = engine.entityEngine().fetchAll(COLLIDERS).size();
        long entityCount = engine.entityEngine().entityCount();
        int fps = engine.loop().metrics().framesPerSecond();
        long updateTime = engine.loop().metrics().durationOfLastUpdate().milliseconds();
        String text = String.format("fps: %d / updatetime %02d / %d entities / %d colliders",
                fps, updateTime, entityCount, colliderCount);
        engine.graphics().window().draw(text(TEXT_POSITION, text, FONT));
    }

    @Override
    public UpdatePriority updatePriority() {
        return UpdatePriority.PRESENTATION_UI_FOREGROUND;
    }
}
