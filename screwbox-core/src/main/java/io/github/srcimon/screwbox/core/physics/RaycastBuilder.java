package io.github.srcimon.screwbox.core.physics;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.Segment;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.entities.Archetype;
import io.github.srcimon.screwbox.core.entities.Component;
import io.github.srcimon.screwbox.core.entities.Entities;
import io.github.srcimon.screwbox.core.entities.Entity;
import io.github.srcimon.screwbox.core.entities.components.ColliderComponent;
import io.github.srcimon.screwbox.core.entities.components.TransformComponent;
import io.github.srcimon.screwbox.core.physics.internal.EntityHasComponentFilter;
import io.github.srcimon.screwbox.core.physics.internal.EntityIsInRaycastFilter;
import io.github.srcimon.screwbox.core.physics.internal.EntityNotInRangeFilter;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public final class RaycastBuilder {

    private final Entities entities;
    private final Vector from;
    private final List<Predicate<Entity>> filters = new ArrayList<>();
    private Borders borders = Borders.ALL;
    private Archetype archetype = Archetype.of(TransformComponent.class, ColliderComponent.class);

    public RaycastBuilder(final Entities entities, final Vector from) {
        this.entities = entities;
        this.from = from;
    }

    public RaycastBuilder checkingBorders(final Borders borders) {
        this.borders = borders;
        return this;
    }

    public RaycastBuilder checkingFor(final Archetype archetype) {
        if (!archetype.contains(TransformComponent.class)) {
            throw new IllegalArgumentException("cannot raycast for Archetypes without TransformComponent");
        }
        this.archetype = archetype;
        return this;
    }

    public RaycastBuilder ignoringEntities(final Entity... ignoredEntities) {
        filters.add(new EntityIsInRaycastFilter(ignoredEntities));
        return this;
    }

    public RaycastBuilder ignoringEntitiesMatching(final Predicate<Entity> filter) {
        filters.add(filter);
        return this;
    }
    public RaycastBuilder ignoringEntitiesHaving(final Class<? extends Component> componentClass) {
        filters.add(new EntityHasComponentFilter(componentClass));
        return this;
    }

    public RaycastBuilder ignoringEntitesNotIn(final Bounds bounds) {
        filters.add(new EntityNotInRangeFilter(bounds));
        return this;
    }

    public Raycast castingTo(final double x, final double y) {
        return castingTo(Vector.of(x, y));
    }

    public Raycast castingTo(final Vector to) {
        final var ray = Segment.between(from, to);
        final List<Entity> matchingEntities = entities.fetchAll(archetype);
        return new Raycast(ray, matchingEntities, filters, borders);
    }

    public Raycast castingVertical(final double length) {
        final Vector to = from.addY(length);
        return castingTo(to);
    }

    public Raycast castingHorizontal(final double length) {
        final Vector to = from.addX(length);
        return castingTo(to);
    }

}
