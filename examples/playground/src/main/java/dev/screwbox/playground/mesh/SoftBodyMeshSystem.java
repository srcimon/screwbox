package dev.screwbox.playground.mesh;

import dev.screwbox.core.Engine;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.softphysics.SoftBodyComponent;
import dev.screwbox.core.environment.softphysics.SoftLinkComponent;
import dev.screwbox.core.environment.softphysics.SoftStructureComponent;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.nonNull;

public class SoftBodyMeshSystem implements EntitySystem {

    private static final Archetype MESHES = Archetype.ofSpacial(SoftBodyMeshComponent.class, SoftBodyComponent.class);

    @Override
    public void update(Engine engine) {
        for (final var meshEntity : engine.environment().fetchAll(MESHES)) {
            final List<Integer> targets = fetchTargets(meshEntity);
        }
    }

    private static List<Integer> fetchTargets(Entity meshEntity) {
        var link = meshEntity.get(SoftLinkComponent.class);

        //TODO use softbody component instead? faster but less reliant?
        final List<Integer> targets = new ArrayList<>();
        if (nonNull(link)) {
            targets.add(link.targetId);
        }
        final var structure = meshEntity.get(SoftStructureComponent.class);
        if (nonNull(structure)) {
            for (final var id : structure.targetIds) {
                targets.add(id);
            }
        }
        return targets;
    }
}
