package de.suzufa.screwbox.core.physics;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import de.suzufa.screwbox.core.Vector;
import de.suzufa.screwbox.core.entityengine.Archetype;
import de.suzufa.screwbox.core.entityengine.Component;
import de.suzufa.screwbox.core.entityengine.Entity;
import de.suzufa.screwbox.core.entityengine.EntityEngine;
import de.suzufa.screwbox.core.entityengine.components.ColliderComponent;
import de.suzufa.screwbox.core.entityengine.components.TransformComponent;
import de.suzufa.screwbox.core.physics.internal.EntityContainsPositionFilter;
import de.suzufa.screwbox.core.physics.internal.EntityHasComponentFilter;
import de.suzufa.screwbox.core.physics.internal.EntitySearchFilter;

public final class SelectEntityBuilder {

    private final List<EntitySearchFilter> filters = new ArrayList<>();
    private final EntityEngine entityEngine;

    private Archetype archetype = Archetype.of(TransformComponent.class, ColliderComponent.class);

    public SelectEntityBuilder(final EntityEngine entityEngine, final Vector position) {
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

    public Optional<Entity> selectAnyEntity() {
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

}
