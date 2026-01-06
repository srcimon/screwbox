package dev.screwbox.playground.mesh;

import dev.screwbox.core.Engine;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.softphysics.SoftBodyComponent;

public class SoftBodyMeshSystem implements EntitySystem {

    private static final Archetype MESHES = Archetype.ofSpacial(SoftBodyMeshComponent.class, SoftBodyComponent.class);

    @Override
    public void update(Engine engine) {
        for (final var meshEntity : engine.environment().fetchAll(MESHES)) {

        }
    }
}
