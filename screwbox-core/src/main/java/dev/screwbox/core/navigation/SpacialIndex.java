package dev.screwbox.core.navigation;

import dev.screwbox.core.HasBounds;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.utils.MathUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import static java.util.Objects.isNull;

/**
 * An dynamic spacial index providing conveniant methods for ultra fast {@link Entity} searches by position.
 * Does not consider {@link Entity#bounds() bounds}.
 *
 * @since 3.27.0
 *
 */
public class SpacialIndex<T extends HasBounds> {

    private static final int NO_INDEX_NEEDED_COUNT = 50;
    private final List<T> entities = new ArrayList<>();
    private SpacialHashRegistry<T> registry;
    private int minCellSize = 2;

    /**
     * Refreshes the {@link Entity entities} that can be searched.
     */
    public void refresh(final List<T> entities) {
        this.entities.clear();
        this.entities.addAll(entities);
        this.registry = null;
    }

    /**
     * Searches {@link Entity entities} within radius to position and also filters result with the provided filter.
     * Internally uses the best suiting method automatically to return the result as fast as possible.
     *
     * @see #findEntities(Vector, double)
     */
    public List<T> findEntities(final Vector position, final double radius, final Predicate<T> entityFilter) {
        final Collection<T> prefetchedEntities = prefetchEntities(position, radius);
        final List<T> nearbyEntities = new ArrayList<>();
        for (final var entity : prefetchedEntities) {
            if (entity.position().distanceTo(position) <= radius && entityFilter.test(entity)) {
                nearbyEntities.add(entity);
            }
        }
        return nearbyEntities;
    }

    /**
     * Searches {@link Entity entities} within radius to position.
     * Internally uses the best suiting method automatically to return the result as fast as possible.
     *
     * @see #findEntities(Vector, double, Predicate)
     */
    public List<T> findEntities(final Vector position, final double radius) {
        final List<T> prefetchedEntities = prefetchEntities(position, radius);
        final List<T> nearbyEntities = new ArrayList<>();
        for (final var entity : prefetchedEntities) {
            if (entity.position().distanceTo(position) <= radius) {
                nearbyEntities.add(entity);
            }
        }
        return nearbyEntities;
    }

    /**
     * Returns the current {@link SpacialHashRegistry registry} that can is utilized for fast entity searches.
     */
    public Optional<SpacialHashRegistry<T>> registry() {
        return Optional.ofNullable(registry);
    }

    private List<T> prefetchEntities(final Vector position, final double radius) {
        if (entities.size() <= NO_INDEX_NEEDED_COUNT) {
            return entities;
        }

        if (isNull(registry) || registry.cellSize() < radius) {
            registry = new SpacialHashRegistry<>(calculateNextCellSize(radius), entities);
        }

        return registry.queryLocalBuckets(position);
    }

    private int calculateNextCellSize(final double radius) {
        final int cellSizeByRadius = MathUtil.nextHighestPowerOfTwoNumber(radius);
        minCellSize = Math.max(minCellSize, cellSizeByRadius);
        return minCellSize;
    }

}
