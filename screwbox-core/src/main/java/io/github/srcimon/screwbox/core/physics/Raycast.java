package io.github.srcimon.screwbox.core.physics;

import io.github.srcimon.screwbox.core.Line;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;
import io.github.srcimon.screwbox.core.utils.ListUtil;

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
        for (final Entity entity : entities) {
            if (isNotFiltered(entity)) {
                for (var intersection : ray.intersections(getLines(entity))) {
                    if (isNull(currentHit) || Double.compare(intersection.distanceTo(ray.from()), currentHit.distanceTo(ray.from())) < 0) {
                        currentHit = intersection;
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
                final var lines = getLines(entity);
                ListUtil.addAll(intersections, ray.intersections((lines)));
            }
        }
        return intersections;
    }

    private List<Line> getLines(final Entity entity) {
        return borders.extractFrom(entity.get(TransformComponent.class).bounds);
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
        for (final Line border : getLines(entity)) {
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
