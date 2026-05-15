package dev.screwbox.playground;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Engine;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.ExecutionOrder;
import dev.screwbox.core.environment.Order;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.internal.LightPhysics;
import dev.screwbox.core.graphics.options.LineDrawOptions;
import dev.screwbox.core.keyboard.Key;

@ExecutionOrder(Order.DEBUG_OVERLAY)
public class IlluminationDebugSystem implements EntitySystem {

    @Override
    public void update(final Engine engine) {
        var bounds = Bounds.atPosition(engine.mouse().position(), 48, 48);
        if(!engine.keyboard().isDown(Key.SPACE)) {
            engine.graphics().light().addIllumination(engine.mouse().position(), 80, Color.BLACK);
        }
        engine.graphics().light().addGlow(engine.mouse().position(), 80, Color.WHITE.opacity(0.1));
        engine.graphics().light().addPointLight(engine.mouse().position(), 120, Color.BLACK);
//        var rays = LightPhysics.DEBUG.calculateIlluminationRays(bounds.position(), 120);
//        for (var ray : rays) {
//            if(ray.reflections()>1) {
//                engine.graphics().world().drawLine(ray.ray(), LineDrawOptions.color(Color.WHITE.opacity(ray.strength())));
//                if (ray.collided() != null) {
//                    //         engine.graphics().world().drawLine(ray.collided(), LineDrawOptions.color(Color.RED.opacity(ray.strength())));
//                }
//            }
//        }
    }
}
