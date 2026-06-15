package dev.screwbox.playground;

import dev.screwbox.core.Engine;
import dev.screwbox.core.Percent;
import dev.screwbox.core.Polygon;
import dev.screwbox.core.audio.SoundBundle;
import dev.screwbox.core.audio.SoundOptions;
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
        double maxLength = 160;
        var traces = new RaytraceAudio().findAllRayHits(engine.mouse().position(), targetPos, engine.environment().fetchAll(Archetype.ofSpacial(ColliderComponent.class)).stream().map(Entity::bounds).toList(), maxLength);
        for(var trace : traces) {
            engine.graphics().world().drawPolygon(trace.polygon(), PolygonDrawOptions.outline(Color.GREEN.mix(Color.RED, Percent.of(trace.length() / maxLength))));
            engine.graphics().world().drawText(trace.polygon().center(), trace.name(), SystemTextDrawOptions.systemFont("Arial"));
        }
        if(engine.mouse().isPressedLeft()) {
            var panDegrees = 0;
            for(var trace : traces) {
                panDegrees+=trace.targetAngle().degrees();
            }
if(traces.size()>0) {
    double pan = getPan(panDegrees / traces.size());
    System.out.println(pan);
    engine.audio().playSound(SoundBundle.JUMP, SoundOptions.playOnce().pan(pan));
            }
        }
    }
}
