package dev.screwbox.playground;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Polygon;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.importing.IdPool;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.core.environment.softphysics.RopeComponent;
import dev.screwbox.core.environment.softphysics.SoftBodyComponent;
import dev.screwbox.core.environment.softphysics.SoftLinkComponent;
import dev.screwbox.core.environment.softphysics.SoftStructureComponent;
import dev.screwbox.core.utils.Validate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class SoftPhysicsSupport {

    private record Link(int start, int end) {

        public static Link create(int a, int b) {
            return a > b ? new Link(a, b) : new Link(b, a);
        }
    }

    private SoftPhysicsSupport() {
    }

    public static List<Entity> createRope(final Vector start, final Vector end, final int nodeCount, final IdPool pool) {
        Validate.range(nodeCount, 3, 4096, "nodeCount must be between 3 and 4096");
        List<Entity> entities = new ArrayList<>();
        Vector spacing = start.substract(end).multiply(1.0 / nodeCount);
        int id = pool.allocateId();
        for (int i = nodeCount; i >= 0; i--) {
            boolean isStart = i == nodeCount;
            boolean isEnd = i == 0;

            Entity add = new Entity(id)
                .bounds(Bounds.atPosition(end.add(spacing.multiply(i)), 1, 1));

            add.add(new PhysicsComponent());

            if (isStart) {
                add.add(new RopeComponent());
            }
            if (!isEnd) {
                add.add(new SoftLinkComponent(pool.peekId()));
            }
            entities.add(add);
            id = pool.allocateId();
        }
        return entities;
    }

    public static List<Entity> createStabilizedSoftBody(final List<Vector> positions, final IdPool idPool) {
        List<Entity> entities = createSoftBody(positions, idPool);
        List<Vector> nodes = new ArrayList<>(entities.stream().map(e -> e.bounds().position()).toList());

        final Set<Link> links = new HashSet<>();
        nodes.add(nodes.getFirst());
        var polygon = Polygon.ofNodes(nodes);
        for (int i = 0; i < polygon.nodeCount(); ++i) {
            var opposingIndex = polygon.opposingIndex(i);
            if (opposingIndex.isPresent()) {
                links.add(Link.create(i, opposingIndex.get()));
            }
        }
        var distinctStarts = links.stream().map(l -> l.start).distinct().toList();
        for (var start : distinctStarts) {
            var targets = fetchTargets(start, links);
            for (int x = 0; x < entities.size(); x++) {
                if (x == start) {
                    entities.get(x).add(new SoftStructureComponent(targets.stream().map(i -> entities.get(i).forceId()).toList()));
                }
            }
        }
        return entities;
    }

    private static List<Integer> fetchTargets(int start, Set<Link> links) {
        List<Integer> targets = new ArrayList<>();
        for(var link : links) {
            if(link.start == start) {
                targets.add(link.end);
            }
        }
        return targets;
    }

    public static List<Entity> createSoftBody(final List<Vector> positions, final IdPool idPool) {
        Validate.range(positions.size(), 3, 1024, "nodeCount must be between 3 and 4096");
        List<Entity> entities = new ArrayList<>();

        positions.stream()
            .map(position -> new Entity(idPool.allocateId()).bounds(Bounds.atPosition(position, 1, 1)).add(new PhysicsComponent()))
            .forEach(entities::add);

        entities.getFirst().add(new SoftBodyComponent());
        for (int i = 0; i < positions.size(); i++) {
            int grabIndex = i + 1;
            if (grabIndex >= positions.size()) {
                grabIndex = 0;
            }
            entities.get(i).add(new SoftLinkComponent(entities.get(grabIndex).forceId()));
        }
        //TODO handle last position = first position
        return entities;
    }
}
