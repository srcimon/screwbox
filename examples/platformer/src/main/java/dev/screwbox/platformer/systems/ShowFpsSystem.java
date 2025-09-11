package dev.screwbox.platformer.systems;

import dev.screwbox.core.Duration;
import dev.screwbox.core.Engine;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.Order;
import dev.screwbox.core.environment.physics.ColliderComponent;
import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.graphics.options.SystemTextDrawOptions;
import dev.screwbox.core.utils.SmoothValue;

@Order(Order.SystemOrder.DEBUG_OVERLAY)
public class ShowFpsSystem implements EntitySystem {

    private static final Archetype COLLIDERS = Archetype.ofSpacial(ColliderComponent.class);
    private static final Offset TEXT_POSITION = Offset.at(50, 50);
    private static final SystemTextDrawOptions OPTIONS = SystemTextDrawOptions.systemFont("Futura", 14);
    private final SmoothValue fps = new SmoothValue(120);
    private final SmoothValue updateMs = new SmoothValue(120);

    @Override
    public void update(Engine engine) {
        updateMs.recordSample(engine.loop().updateDuration().nanos());
        String text = "fps: %.0f  / %d entities (%d colliders) / %d systems / %s".formatted(
                fps.average(engine.loop().fps()),
                engine.environment().entityCount(),
                engine.environment().fetchAll(COLLIDERS).size(),
                engine.environment().systems().size(),
                Duration.ofNanos((long)updateMs.average()).humanReadable());
        engine.graphics().canvas().drawText(TEXT_POSITION, text, OPTIONS);
    }
}
