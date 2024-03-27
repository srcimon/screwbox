package io.github.srcimon.screwbox.examples.platformer.systems;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.Order;
import io.github.srcimon.screwbox.core.environment.SystemOrder;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;
import io.github.srcimon.screwbox.core.environment.physics.ColliderComponent;
import io.github.srcimon.screwbox.core.graphics.Offset;
import io.github.srcimon.screwbox.core.graphics.TextDrawOptions;

@Order(SystemOrder.PRESENTATION_UI_FOREGROUND)
public class ShowFpsSystem implements EntitySystem {

    private static final Archetype COLLIDERS = Archetype.of(ColliderComponent.class, TransformComponent.class);
    private static final Offset TEXT_POSITION = Offset.at(50, 50);
    private static final TextDrawOptions OPTIONS = TextDrawOptions.systemFont("Futura", 14);

    @Override
    public void update(Engine engine) {
        String text = "fps: %04d  / %d entities (%d colliders) / %d systems / %s".formatted(
                engine.loop().fps(),
                engine.environment().entityCount(),
                engine.environment().fetchAll(COLLIDERS).size(),
                engine.environment().systems().size(),
                engine.loop().updateDuration().humanReadable());
        engine.graphics().screen().drawText(TEXT_POSITION, text, OPTIONS);
    }
}
