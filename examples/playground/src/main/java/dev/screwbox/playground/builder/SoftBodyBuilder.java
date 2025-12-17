package dev.screwbox.playground.builder;

import dev.screwbox.core.Polygon;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.Environment;
import dev.screwbox.core.environment.core.TransformComponent;
import dev.screwbox.core.environment.fluids.FloatComponent;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.core.environment.softphysics.SoftBodyComponent;
import dev.screwbox.core.environment.softphysics.SoftBodyRenderComponent;
import dev.screwbox.core.environment.softphysics.SoftBodyShapeComponent;
import dev.screwbox.core.environment.softphysics.SoftLinkComponent;
import dev.screwbox.core.environment.softphysics.SoftStructureComponent;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.environment.softphysics.SoftBodyCollisionComponent;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SoftBodyBuilder {

    public final List<Vector> nodes = new ArrayList<>();

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
            entity.add(new TransformComponent(node,2,2));
            entity.add(new FloatComponent());
            entity.add(new PhysicsComponent(), p -> p.friction = 3);
            entity.add(new SoftLinkComponent(isLast ? environment.peekId() : entities.getFirst().id().get()));
            entities.add(entity);
            if(isFirst) {
                entity.add(new SoftBodyComponent());
                entity.add(new SoftBodyShapeComponent());
                entity.add(new SoftBodyCollisionComponent());
                entity.add(new SoftBodyRenderComponent(Color.random()), s -> {
                    s.outlineColor = Color.random().opacity(0.5);
                    s.outlineStrokeWidth = 3;
                });
            }
        }

        final Set<Link> links = new HashSet<>();
        nodes.add(nodes.getFirst());
        var polygon = Polygon.ofNodes(nodes);
        for(int i = 0; i < polygon.nodeCount(); ++i) {
            var opposingIndex = polygon.opposingIndex(i);
            if(opposingIndex.isPresent()) {
                links.add(Link.create(i, opposingIndex.get()));
            }
        }
        var distinctStarts = links.stream().map(l -> l.start).distinct().toList();
        for(var start : distinctStarts) {
           var targets =  fetchTargets(start, links);
           for(int x = 0; x < entities.size(); x++) {
               if(x ==start) {
                   entities.get(x).add(new SoftStructureComponent(targets.stream().map(i -> entities.get(i).id().get()).toList()));
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
