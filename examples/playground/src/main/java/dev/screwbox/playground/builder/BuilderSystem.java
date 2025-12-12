package dev.screwbox.playground.builder;

import dev.screwbox.core.Engine;
import dev.screwbox.core.environment.EntitySystem;

public class BuilderSystem implements EntitySystem {

    SoftBodyBuilder builder = new SoftBodyBuilder();

    @Override
    public void update(Engine engine) {
        if (engine.mouse().isPressedLeft()) {
            builder.addNode(engine.mouse().position());
        }
        if(engine.mouse().isPressedRight()) {
            builder.addTo(engine.environment());
            builder = new SoftBodyBuilder();
        }
    }
}
