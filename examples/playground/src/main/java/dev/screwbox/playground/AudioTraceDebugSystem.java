package dev.screwbox.playground;

import dev.screwbox.core.Engine;
import dev.screwbox.core.Polygon;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.physics.ColliderComponent;
import dev.screwbox.core.environment.rendering.CameraTargetComponent;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.options.PolygonDrawOptions;
import dev.screwbox.core.graphics.options.SystemTextDrawOptions;

public class AudioTraceDebugSystem implements EntitySystem {
    @Override
    public void update(Engine engine) {
        var targetPos = engine.environment().fetchSingleton(CameraTargetComponent.class).position();
        var traces = new RaytraceAudio().findAllRayHits(engine.mouse().position(), targetPos, engine.environment().fetchAll(Archetype.ofSpacial(ColliderComponent.class)).stream().map(Entity::bounds).toList(), 160);
        for(var trace : traces) {
            engine.graphics().world().drawPolygon(trace.polygon(), PolygonDrawOptions.outline(Color.YELLOW.opacity(0.5)));
            engine.graphics().world().drawText(trace.polygon().center(), trace.name(), SystemTextDrawOptions.systemFont("Arial"));
        }
    }
}
