package dev.screwbox.playground;

import dev.screwbox.core.Engine;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.ExecutionOrder;

import static dev.screwbox.core.environment.Order.PRESENTATION_WORLD;

@ExecutionOrder(PRESENTATION_WORLD)
public class ClothRenderSystem implements EntitySystem {

    //TODO canvas().renderMesh(Mesh())
//TODO algorithm is called mesh shading!
    private static final Archetype CLOTHS = Archetype.ofSpacial(ClothRenderComponent.class, ClothComponent.class);

    @Override
    public void update(Engine engine) {
        for (final var cloth : engine.environment().fetchAll(CLOTHS)) {
            Entity[][] mesh = cloth.get(ClothComponent.class).mesh;

        }
    }
}
