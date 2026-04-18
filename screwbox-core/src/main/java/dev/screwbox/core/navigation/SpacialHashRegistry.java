package dev.screwbox.core.navigation;

import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.utils.MathUtil;
import dev.screwbox.core.utils.Validate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.Collections.emptyList;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

//TODO changelog
/**
 * A registry that stores {@link Entity entities} by a hash of their position. Can speed up searches by position within
 * a large list of {@link Entity entities}.
 *
 * @see 3.27.0
 */
public class SpacialHashRegistry {

    private final double cellSize;
    private final int tableSizeMinusOne;
    private final List<Entity>[] entityTable;

    public SpacialHashRegistry(final double cellSize, final List<Entity> entities) {
        Validate.positive(cellSize, "cell size must be positive");
        this.cellSize = cellSize;
        // tableSize must be 2^x to avoid cpu heavy modulo on index calculation
        final var tableSize = MathUtil.nextHighestPowerOfTwoNumber(entities.size() * 2);

        entityTable = (List<Entity>[]) new List[tableSize];
        this.tableSizeMinusOne = entityTable.length - 1;
        for (final var entity : entities) {
            Vector position = entity.position();
            final int index = createIndex(position);
            registerToKey(entity, index);
        }
    }

    /**
     * Returns all {@link Entity entities} within the bucket specified by the position.
     * May also contain {@link Entity entities} far away from the position, so manual filtering may be required
     * depending on the use case.
     */
    public List<Entity> queryBucket(final Vector position) {
        final int index = createIndex(position);
        final List<Entity> entities = entityTable[index];
        return isNull(entities)
            ? emptyList()
            : entities;
    }

    /**
     * Returns all {@link Entity entities} within the bucket specified by the position as well as the surrounding buckets.
     * May also contain {@link Entity entities} far away from the position, so manual filtering may be required
     * depending on the use case.
     */
    public List<Entity> queryLocalBuckets(final Vector position) {
        final List<Entity> found = new ArrayList<>();
        final long gridX = toGrid(position.x());
        final long gridY = toGrid(position.y());
        final Set<Integer> alreadyProcessedIndexes = new HashSet<>();

        for (long x = gridX - 1; x <= gridX + 1; x++) {
            for (long y = gridY - 1; y <= gridY + 1; y++) {
                int index = createIndex(x, y);
                if (!alreadyProcessedIndexes.contains(index)) {
                    alreadyProcessedIndexes.add(index);
                    final List<Entity> entities = entityTable[index];
                    if (nonNull(entities)) {
                        found.addAll(entities);
                    }
                }

            }
        }
        return found;
    }

    /**
     * Returns the cell size of the hash.
     */
    public double cellSize() {
        return cellSize;
    }

    private int createIndex(final Vector position) {
        final long gridX = toGrid(position.x());
        final long gridY = toGrid(position.y());
        return createIndex(gridX, gridY);
    }

    private int createIndex(final long x, final long y) {
        final long value = (x * 73856093L) ^ (y * 19349663L);
        return (int) (value & tableSizeMinusOne); // requires tableSize to be 2^x
    }

    private long toGrid(final double value) {
        return (long) Math.floor(value / cellSize);
    }

    private void registerToKey(final Entity entity, final int key) {
        List<Entity> entities = entityTable[key];
        if (isNull(entities)) {
            entities = new ArrayList<>();
        }
        entities.add(entity);
        entityTable[key] = entities;
    }

}
