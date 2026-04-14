package dev.screwbox.playground.misc;

import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class SpacialHash {

    private final double cellSize;
    private int tableSizeMinusOne;
    private List<Entity>[] entityTable;

    @SuppressWarnings("unchecked")
    public SpacialHash(double cellSize) {
        this.cellSize = cellSize;
    }

    public void rebuild(final List<Entity> entities) {
        // tableSize must be 2^x to avoid cpu heavy modulo on index calculation
        final var tableSize = nextHighestPowerOfTwoNumber(entities.size() * 2);

        entityTable = (List<Entity>[]) new List[tableSize];
        this.tableSizeMinusOne = entityTable.length - 1;
        for (final var entity : entities) {
            final long gridX = toGrid(entity.position().x());
            final long gridY = toGrid(entity.position().y());

            int index = createIndex(gridX, gridY);
            registerToKey(entity, index);
        }
    }

    public List<Entity> queryBucket(final Vector position) {
        final long x = toGrid(position.x());
        final long y = toGrid(position.y());
        final List<Entity> entities = entityTable[createIndex(x, y)];
        return isNull(entities)
            ? Collections.emptyList()
            : entities;
    }

    public List<Entity> queryLocalBuckets(final Vector position) {
        final List<Entity> found = new ArrayList<>();
        final long gridX = toGrid(position.x());
        final long gridY = toGrid(position.y());

        for (long x = gridX - 1; x <= gridX + 1; x++) {
            for (long y = gridY - 1; y <= gridY + 1; y++) {
                final List<Entity> entities = entityTable[createIndex(x, y)];
                if (nonNull(entities)) {
                    found.addAll(entities);
                }
            }
        }
        return found;
    }

    private int createIndex(final long x, final long y) {
        final long value = (x * 73856093L) ^ (y * 19349663L);
        return (int) (value & (tableSizeMinusOne)); // requires tableSize to be 2^x
    }

    private long toGrid(final double value) {
        return (long) Math.floor(value / cellSize);
    }

    private static int nextHighestPowerOfTwoNumber(final int doubleEntityCount) {
        return Integer.highestOneBit(doubleEntityCount - 1) << 1;
    }

    private void registerToKey(Entity entity, int key) {
        List<Entity> entities = entityTable[key];
        if (Objects.isNull(entities)) {
            entities = new ArrayList<>();
        }
        entities.add(entity);
        entityTable[key] = entities;
    }

}
