package dev.screwbox.core.navigation;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.Component;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.Environment;
import dev.screwbox.core.environment.core.TransformComponent;
import dev.screwbox.core.navigation.internal.EntityContainsPositionFilter;
import dev.screwbox.core.navigation.internal.EntityHasComponentFilter;
import dev.screwbox.core.navigation.internal.EntityNotInRangeFilter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public final class SelectEntityBuilder {

    private final List<Predicate<Entity>> filters = new ArrayList<>();
    private final Environment environment;

    private Archetype archetype = Archetype.ofSpacial();

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
