package dev.screwbox.playground.misc;

import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.util.Objects.nonNull;

public class SpacialHash {

    private final double cellSize;
    private final int tableSizeMinusOne;
    private final List<Entity>[] register;

    @SuppressWarnings("unchecked")
    public SpacialHash(double cellSize, final List<Entity> entities) {
        this.cellSize = cellSize;

        // tableSize must be 2^x to avoid cpu heavy modulo on index calculation
        final var tableSize = nextHighestPowerOfTwoNumber(entities.size() * 2);

        register = (List<Entity>[]) new List[tableSize];
        this.tableSizeMinusOne = register.length - 1;
        for (final var entity : entities) {
            final long gridX = toGrid(entity.position().x());
            final long gridY = toGrid(entity.position().y());

            int index = createIndex(gridX, gridY);
            registerToKey(entity, index);
        }
    }


    public List<Entity> findInSingleCell(final Vector position) {
        final long gridX = toGrid(position.x());
        final long gridY = toGrid(position.y());
        List<Entity> c = register[createIndex(gridX, gridY)];
        return nonNull(c) ? c : Collections.emptyList();
    }

    public List<Entity> findInSurroundingCells(final Vector position) {
        final List<Entity> found = new ArrayList<>();
        final long gridX = toGrid(position.x());
        final long gridY = toGrid(position.y());

        for (long x = gridX - 1; x <= gridX + 1; x++) {
            for (long y = gridY - 1; y <= gridY + 1; y++) {
                List<Entity> c = register[createIndex(x, y)];
                if (nonNull(c)) {
                    found.addAll(c);
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
        List<Entity> reg = register[key];
        if (reg == null) {
            reg = new ArrayList<>();
        }
        reg.add(entity);
        register[key] = reg;
    }

}
