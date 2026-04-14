package dev.screwbox.playground.misc;

import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Entity;

import java.util.ArrayList;
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
            final long gridX = (long) Math.floor(entity.position().x() / cellSize);
            final long gridY = (long) Math.floor(entity.position().y() / cellSize);

            int index = createIndex(gridX, gridY);
            registerToKey(entity, index);
        }
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


    public List<Entity> findAllNeighbors(final Vector position) {
        final List<Entity> found = new ArrayList<>();
        final long gridX = (long) Math.floor(position.x() / cellSize);
        final long gridY = (long) Math.floor(position.y() / cellSize);

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
}
