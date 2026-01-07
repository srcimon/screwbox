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

    private record Connection(Integer start, Integer end) {}

    @Override
    public void update(Engine engine) {
        for (final var meshEntity : engine.environment().fetchAll(MESHES)) {
            List<Connection> connections = fetchAllConnections(engine, meshEntity);
            System.out.println(connections.size());
        }
    }

    private static List<Connection> fetchAllConnections(Engine engine, Entity meshEntity) {
        List<Connection> connections = new ArrayList<>();
        List<Integer> processedEntities = new ArrayList<>();
        enrichConnections(meshEntity.forceId(), connections, processedEntities, engine.environment());
        return connections;
    }

    private static void enrichConnections(int startId, List<Connection> connections, List<Integer> processedEntities, Environment environment) {
        var startEntity = environment.fetchById(startId);
        final List<Integer> targets = fetchTargetsFromSingleEntity(startEntity);
        targets.stream()
            .map(target -> new Connection(startId, target))
            .forEach(connections::add);

        processedEntities.add(startId);
        for(final var target : targets) {
            if(!processedEntities.contains(target)) {
                enrichConnections(target, connections, processedEntities, environment);
            }
        }
    }

    private static List<Integer> fetchTargetsFromSingleEntity(Entity meshEntity) {
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
