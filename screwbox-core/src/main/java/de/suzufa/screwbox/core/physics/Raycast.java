package de.suzufa.screwbox.core.physics;

import static java.util.Collections.sort;
import static java.util.Objects.nonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import de.suzufa.screwbox.core.Segment;
import de.suzufa.screwbox.core.Vector;
import de.suzufa.screwbox.core.entityengine.Entity;
import de.suzufa.screwbox.core.entityengine.components.TransformComponent;
import de.suzufa.screwbox.core.physics.internal.DistanceComparator;
import de.suzufa.screwbox.core.physics.internal.EntitySearchFilter;
import de.suzufa.screwbox.core.physics.internal.ExtractSegmentsMethod;

public class Raycast {

    private final Segment ray;
    private final List<Entity> entities;
    private final List<EntitySearchFilter> filters;
    private final ExtractSegmentsMethod method;

    Raycast(final Segment ray, final List<Entity> entities, final List<EntitySearchFilter> filters,
            final ExtractSegmentsMethod method) {
        this.ray = ray;
        this.entities = entities;
        this.filters = filters;
        this.method = method;
    }

    public Optional<Vector> nearestHit() {
        final List<Vector> hits = findHits();
        if (hits.isEmpty()) {
            return Optional.empty();
        }
        sort(hits, new DistanceComparator(ray.from()));
        return Optional.of(hits.get(0));
    }

    public Optional<Entity> selectAnyEntity() {
        for (final Entity entity : entities) {
            if (isNotFiltered(entity) && intersectsRay(entity)) {
                return Optional.of(entity);
            }
        }
        return Optional.empty();
    }

    public List<Entity> selectAllEntities() {
        final List<Entity> selected = new ArrayList<>();
        for (final Entity entity : entities) {
            if (isNotFiltered(entity) && intersectsRay(entity)) {
                selected.add(entity);
            }
        }
        return selected;
    }

    public List<Vector> findHits() {
        final List<Vector> intersections = new ArrayList<>();
        for (final Entity entity : entities) {
            if (isNotFiltered(entity)) {
                intersections.addAll(getIntersections(entity));
            }
        }
        return intersections;
    }

    public boolean hasHit() {
        for (final Entity entity : entities) {
            if (isNotFiltered(entity) && intersectsRay(entity)) {
                return true;
            }
        }
        return false;
    }

    private List<Vector> getIntersections(final Entity entity) {
    	List<Vector> intersections = new ArrayList<>();
        for (final Segment border : method.segments(entity.get(TransformComponent.class).bounds)) {
            final Vector intersectionPoint = ray.intersectionPoint(border);
            if (nonNull(intersectionPoint)) {
                 intersections.add(intersectionPoint);
            }
        }
        return intersections;
    }

    private boolean intersectsRay(final Entity entity) {
        for (final Segment border : method.segments(entity.get(TransformComponent.class).bounds)) {
            if (ray.intersects(border)) {
                return true;
            }
        }
        return false;
    }

    private boolean isNotFiltered(final Entity entity) {
        for (final EntitySearchFilter filter : filters) {
            if (filter.matches(entity)) {
                return false;
            }
        }
        return true;
    }

    public boolean noHit() {
        return !hasHit();
    }

}
