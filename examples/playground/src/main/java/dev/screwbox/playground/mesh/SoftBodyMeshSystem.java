package dev.screwbox.playground.mesh;

import dev.screwbox.core.Engine;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.Environment;
import dev.screwbox.core.environment.softphysics.SoftBodyComponent;
import dev.screwbox.core.environment.softphysics.SoftLinkComponent;
import dev.screwbox.core.environment.softphysics.SoftStructureComponent;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.nonNull;

public class SoftBodyMeshSystem implements EntitySystem {

    private static final Archetype MESHES = Archetype.ofSpacial(SoftBodyMeshComponent.class, SoftBodyComponent.class);

    @Override
    public void update(final Engine engine) {
        for (final var meshEntity : engine.environment().fetchAll(MESHES)) {
            Mesh<Entity> mesh = fetchAllConnections(engine, meshEntity);
            System.out.println(mesh.connectionCount());
        }
    }


    private static Mesh<Entity> fetchAllConnections(Engine engine, Entity meshEntity) {
        Mesh<Entity> mesh = new Mesh<>();
        List<Integer> processedEntities = new ArrayList<>();
        enrichConnections(meshEntity.forceId(), mesh, processedEntities, engine.environment());
        return mesh;
    }

    private static void enrichConnections(int startId, Mesh<Entity> mesh, List<Integer> processedEntities, Environment environment) {
        var startEntity = environment.fetchById(startId);
        final List<Integer> targetIds = fetchTargetsFromSingleEntity(startEntity);
        for(final var targetId : targetIds) {
            Entity end = environment.fetchById(targetId);
            mesh.addConnection(startEntity, end);
        }
        processedEntities.add(startId);

        for (final var targetId : targetIds) {
            if (!mesh.hasStartNode(environment.fetchById(targetId))) {//TODO entity is fetched twice
                enrichConnections(targetId, mesh, processedEntities, environment);
            }
        }
    }

    private static List<Integer> fetchTargetsFromSingleEntity(Entity meshEntity) {
        var link = meshEntity.get(SoftLinkComponent.class);

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
