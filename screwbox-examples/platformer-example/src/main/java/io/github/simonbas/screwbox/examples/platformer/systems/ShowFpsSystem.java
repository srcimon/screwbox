package io.github.simonbas.screwbox.examples.platformer.systems;

import io.github.simonbas.screwbox.core.Engine;
import io.github.simonbas.screwbox.core.entities.Archetype;
import io.github.simonbas.screwbox.core.entities.EntitySystem;
import io.github.simonbas.screwbox.core.entities.Order;
import io.github.simonbas.screwbox.core.entities.SystemOrder;
import io.github.simonbas.screwbox.core.entities.components.ColliderComponent;
import io.github.simonbas.screwbox.core.entities.components.TransformComponent;
import io.github.simonbas.screwbox.core.graphics.Color;
import io.github.simonbas.screwbox.core.graphics.Font;
import io.github.simonbas.screwbox.core.graphics.Offset;

@Order(SystemOrder.PRESENTATION_UI_FOREGROUND)
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
        String text = String.format("fps: %04d / updatetime %02d / %d entities / %d colliders",
                fps, updateTime, entityCount, colliderCount);
        engine.graphics().screen().drawText(TEXT_POSITION, text, FONT, Color.WHITE);
    }
}
