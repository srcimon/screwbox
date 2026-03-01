package dev.screwbox.playground.misc;

import dev.screwbox.core.Engine;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.physics.PhysicsComponent;

public class InteractionSystem implements EntitySystem {

    private Entity grabbed;

    @Override
    public void update(Engine engine) {
        if (engine.mouse().isPressedLeft()) {
            engine.navigation().searchAtPosition(engine.mouse().position())
                .checkingFor(Archetype.ofSpacial(PhysicsComponent.class))
                .selectAll().forEach(e -> grabbed = e);
        }

        if (grabbed != null) {
            grabbed.get(PhysicsComponent.class).velocity = Vector.zero();
            grabbed.moveTo(engine.mouse().position());
        }
        if (!engine.mouse().isDownLeft()) {
            grabbed = null;
        }
        for (final var viewport : engine.graphics().viewports()) {
            viewport.camera().move(engine.keyboard().wsadMovement(engine.loop().delta(300)));
        }
    }
}
