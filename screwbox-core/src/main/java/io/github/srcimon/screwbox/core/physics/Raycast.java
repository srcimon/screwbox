package io.github.srcimon.screwbox.core.physics;

import io.github.srcimon.screwbox.core.Line;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.components.TransformComponent;
import io.github.srcimon.screwbox.core.physics.internal.DistanceComparator;
import io.github.srcimon.screwbox.core.utils.ListUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import static java.util.Objects.nonNull;

public class Raycast {

    private final Line ray;
    private final List<Entity> entities;
    private final List<Predicate<Entity>> filters;
    private final Borders borders;

    Raycast(final Line ray, final List<Entity> entities, final List<Predicate<Entity>> filters,
            final Borders borders) {
        this.ray = ray;
        this.entities = entities;
        this.filters = filters;
        this.borders = borders;
    }

    public Optional<Vector> nearestHit() {
        final List<Vector> hits = findHits();
        if (hits.isEmpty()) {
            return Optional.empty();
        }
        hits.sort(new DistanceComparator(ray.from()));
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
                ListUtil.addAll(intersections, getIntersections(entity));
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
        for (final Line border : borders.extractBorders(entity.get(TransformComponent.class).bounds)) {
            final Vector intersectionPoint = ray.intersectionPoint(border);
            if (nonNull(intersectionPoint)) {
                intersections.add(intersectionPoint);
            }
        }
        return intersections;
    }

    private boolean intersectsRay(final Entity entity) {
        for (final Line border : borders.extractBorders(entity.get(TransformComponent.class).bounds)) {
            if (ray.intersects(border)) {
                return true;
            }
        }
        return false;
    }

    private boolean isNotFiltered(final Entity entity) {
        for (final Predicate<Entity> filter : filters) {
            if (filter.test(entity)) {
                return false;
            }
        }
        return true;
    }

    public boolean noHit() {
        return !hasHit();
    }

}
