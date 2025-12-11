package dev.screwbox.playground.builder;

import dev.screwbox.core.Polygon;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.Environment;
import dev.screwbox.core.environment.core.TransformComponent;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.core.environment.softphysics.SoftBodyComponent;
import dev.screwbox.core.environment.softphysics.SoftBodyRenderComponent;
import dev.screwbox.core.environment.softphysics.SoftLinkComponent;
import dev.screwbox.core.environment.softphysics.SoftStructureComponent;
import dev.screwbox.core.graphics.Color;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SoftBodyBuilder {

    private final List<Vector> nodes = new ArrayList<>();

    public void addNode(Vector vector) {
        nodes.add(vector);
    }

    record Link(int start, int end) {

        public static Link create(int a , int b) {
           return a > b ? new Link(a, b) : new Link(b, a);
        }
    }

    public void addTo(Environment environment) {
        final List<Entity> entities = new ArrayList<>();

        for (int i = 0; i < nodes.size(); ++i) {
            boolean isFirst = i == 0;
            boolean isLast = i != nodes.size() - 1;

            var node = nodes.get(i);
            var entity = new Entity(environment.allocateId());
            entity.add(new TransformComponent(node));
            entity.add(new PhysicsComponent());
            entity.add(new SoftLinkComponent(isLast ? environment.peekId() : entities.getFirst().id().get()));
            entities.add(entity);
            if(isFirst) {
                entity.add(new SoftBodyComponent());
                entity.add(new SoftBodyRenderComponent(Color.MAGENTA));
            }
        }

        final Set<Link> links = new HashSet<>();
        var polygon = Polygon.ofNodes(nodes);
        for(int i = 0; i < polygon.nodes().size(); ++i) {
            var opposingIndex = polygon.opposingIndex(i);
            if(opposingIndex.isPresent()) {
                links.add(Link.create(i, opposingIndex.get()));
            }
        }
        var distinctStarts = links.stream().map(l -> l.start).distinct().toList();
        for(var start : distinctStarts) {
           var targets =  fetchTargets(start, links);
           for(final var entity : entities) {
               if(entity.id().get().equals(start)) {
                   entity.add(new SoftStructureComponent(targets));
               }
           }
        }

        environment.addEntities(entities);
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
}
