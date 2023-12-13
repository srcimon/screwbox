package io.github.srcimon.screwbox.examples.platformer.systems;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.Order;
import io.github.srcimon.screwbox.core.environment.SystemOrder;
import io.github.srcimon.screwbox.core.environment.physics.ColliderComponent;
import io.github.srcimon.screwbox.core.environment.components.TransformComponent;
import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.Font;
import io.github.srcimon.screwbox.core.graphics.Offset;

@Order(SystemOrder.PRESENTATION_UI_FOREGROUND)
public class ShowFpsSystem implements EntitySystem {

    private static final Archetype COLLIDERS = Archetype.of(ColliderComponent.class, TransformComponent.class);
    private static final Offset TEXT_POSITION = Offset.at(50, 50);
    private static final Font FONT = new Font("Futura", 14);

    @Override
    public void update(Engine engine) {
        String text = "fps: %04d / updatetime %02d / %d entities (%d colliders) / %d systems".formatted(
                engine.loop().fps(),
                engine.loop().updateDuration().milliseconds(),
                engine.environment().entityCount(),
                engine.environment().fetchAll(COLLIDERS).size(),
                engine.environment().systems().size());
        engine.graphics().screen().drawText(TEXT_POSITION, text, FONT, Color.WHITE);
    }
}
