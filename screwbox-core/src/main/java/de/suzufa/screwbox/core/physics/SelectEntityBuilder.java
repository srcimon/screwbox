package de.suzufa.screwbox.core.physics;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import de.suzufa.screwbox.core.Bounds;
import de.suzufa.screwbox.core.Vector;
import de.suzufa.screwbox.core.entities.Archetype;
import de.suzufa.screwbox.core.entities.Component;
import de.suzufa.screwbox.core.entities.Entity;
import de.suzufa.screwbox.core.entities.Entities;
import de.suzufa.screwbox.core.entities.components.ColliderComponent;
import de.suzufa.screwbox.core.entities.components.TransformComponent;
import de.suzufa.screwbox.core.physics.internal.EntityContainsPositionFilter;
import de.suzufa.screwbox.core.physics.internal.EntityHasComponentFilter;
import de.suzufa.screwbox.core.physics.internal.EntityNotInRangeFilter;
import de.suzufa.screwbox.core.physics.internal.EntitySearchFilter;

public final class SelectEntityBuilder {

    private final List<EntitySearchFilter> filters = new ArrayList<>();
    private final Entities entityEngine;

    private Archetype archetype = Archetype.of(TransformComponent.class, ColliderComponent.class);

    public SelectEntityBuilder(final Entities entityEngine, final Bounds bounds) {
        this.entityEngine = entityEngine;
        filters.add(new EntityNotInRangeFilter(bounds));
    }

    public SelectEntityBuilder(final Entities entityEngine, final Vector position) {
        this.entityEngine = entityEngine;
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
        for (final Entity entity : entityEngine.fetchAll(archetype)) {
            if (isNotFiltered(entity)) {
                return Optional.of(entity);
            }
        }
        return Optional.empty();
    }

    private boolean isNotFiltered(final Entity entity) {
        for (final EntitySearchFilter filter : filters) {
            if (filter.matches(entity)) {
                return false;
            }
        }
        return true;
    }

    public List<Entity> selectAll() {
        var entities = new ArrayList<Entity>();
        for (final Entity entity : entityEngine.fetchAll(archetype)) {
            if (isNotFiltered(entity)) {
                entities.add(entity);
            }
        }
        return entities;
    }

}
