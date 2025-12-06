package dev.screwbox.core.navigation;

import dev.screwbox.core.Line;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import static java.util.Objects.isNull;

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
        Vector currentHit = null;
        double currentDistance = Double.MAX_VALUE;
        for (final Entity entity : entities) {
            if (isNotFiltered(entity)) {
                for (final var intersection : ray.intersections(borders.extractFrom(entity.bounds()))) {
                    final double distance = intersection.distanceTo(ray.start());
                    if (distance < currentDistance) {
                        currentHit = intersection;
                        currentDistance = distance;
                    }
                }
            }
        }
        return Optional.ofNullable(currentHit);
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
                final var lines = borders.extractFrom(entity.bounds());
                intersections.addAll(ray.intersections(lines));
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

    public Line ray() {
        return ray;
    }

    private boolean intersectsRay(final Entity entity) {
        for (final Line border : borders.extractFrom(entity.bounds())) {
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

    /**
     * Returns the nearest {@link Entity} found by this {@link Raycast}. Will be empty when not hit was found.
     *
     * @since 2.10.0
     */
    public Optional<Entity> nearestEntity() {
        Vector currentHit = null;
        Entity currentEntity = null;
        for (final Entity entity : entities) {
            if (isNotFiltered(entity)) {
                for (var intersection : ray.intersections(borders.extractFrom(entity.bounds()))) {
                    if (isNull(currentHit) || Double.compare(intersection.distanceTo(ray.start()), currentHit.distanceTo(ray.start())) < 0) {
                        currentHit = intersection;
                        currentEntity = entity;
                    }
                }
            }
        }
        return Optional.ofNullable(currentEntity);
    }
}
