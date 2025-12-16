package dev.screwbox.playground.builder;

import dev.screwbox.core.Engine;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.options.OvalDrawOptions;
import dev.screwbox.core.scenes.animations.CirclesAnimation;

public class BuilderSystem implements EntitySystem {

    SoftBodyBuilder builder = new SoftBodyBuilder();

    @Override
    public void update(Engine engine) {
        builder.nodes.forEach(n -> {
            engine.graphics().world().drawCircle(n, 2, OvalDrawOptions.filled(Color.WHITE.opacity(0.1)));
        });
        if (engine.mouse().isPressedLeft()) {
            builder.addNode(engine.mouse().position());
        }
        if(engine.mouse().isPressedRight()) {
            builder.addTo(engine.environment());
            builder = new SoftBodyBuilder();
        }
    }
}
