package io.github.srcimon.screwbox.core.physics;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.Component;
import io.github.srcimon.screwbox.core.environment.Environment;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.physics.ColliderComponent;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;
import io.github.srcimon.screwbox.core.physics.internal.EntityContainsPositionFilter;
import io.github.srcimon.screwbox.core.physics.internal.EntityHasComponentFilter;
import io.github.srcimon.screwbox.core.physics.internal.EntityNotInRangeFilter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public final class SelectEntityBuilder {

    private final List<Predicate<Entity>> filters = new ArrayList<>();
    private final Environment environment;

    private Archetype archetype = Archetype.of(TransformComponent.class, ColliderComponent.class);

    public SelectEntityBuilder(final Environment environment, final Bounds bounds) {
        this.environment = environment;
        filters.add(new EntityNotInRangeFilter(bounds));
    }

    public SelectEntityBuilder(final Environment environment, final Vector position) {
        this.environment = environment;
        filters.add(new EntityContainsPositionFilter(position));
    }

    public SelectEntityBuilder checkingFor(final Archetype archetype) {
        if (!archetype.contains(TransformComponent.class)) {
            throw new IllegalArgumentException(
                    "cannot select entities by position for Archetypes without TransformComponent");
        }
        this.archetype = archetype;
        return this;
    }

    public SelectEntityBuilder ignoringEntitiesHaving(final Class<? extends Component> componentClass) {
        filters.add(new EntityHasComponentFilter(componentClass));
        return this;
    }

    public Optional<Entity> selectAny() {
        for (final Entity entity : environment.fetchAll(archetype)) {
            if (isNotFiltered(entity)) {
                return Optional.of(entity);
            }
        }
        return Optional.empty();
    }

    private boolean isNotFiltered(final Entity entity) {
        for (final Predicate<Entity> filter : filters) {
            if (filter.test(entity)) {
                return false;
            }
        }
        return true;
    }

    public List<Entity> selectAll() {
        var selectedEntities = new ArrayList<Entity>();
        for (final Entity foundEntities : environment.fetchAll(archetype)) {
            if (isNotFiltered(foundEntities)) {
                selectedEntities.add(foundEntities);
            }
        }
        return selectedEntities;
    }

}
