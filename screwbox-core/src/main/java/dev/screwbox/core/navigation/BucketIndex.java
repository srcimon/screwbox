package dev.screwbox.core.navigation;

import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.utils.MathUtil;
import dev.screwbox.core.utils.Validate;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

//TODO add to utils doc
//TODO document
//TODO changelog
//TODO move to navigation?
public class BucketIndex {

    private final double cellSize;
    private final int tableSizeMinusOne;
    private final List<Entity>[] entityTable;

    public BucketIndex(final double cellSize, final List<Entity> entities) {
        Validate.positive(cellSize, "cell size must be positive");
        this.cellSize = cellSize;
        // tableSize must be 2^x to avoid cpu heavy modulo on index calculation
        final var tableSize = MathUtil.nextHighestPowerOfTwoNumber(entities.size() * 2);

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
            ? emptyList()
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

    public double cellSize() {
        return cellSize;
    }

    private int createIndex(final long x, final long y) {
        final long value = (x * 73856093L) ^ (y * 19349663L);
        return (int) (value & (tableSizeMinusOne)); // requires tableSize to be 2^x
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
