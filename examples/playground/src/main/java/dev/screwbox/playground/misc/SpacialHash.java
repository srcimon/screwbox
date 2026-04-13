package dev.screwbox.playground.misc;

import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Entity;

import java.util.ArrayList;
import java.util.List;

public class SpacialHash {


    private final double cellSize;
    private final int tableSize;
    private final List<Entity>[] register;

    public SpacialHash(double cellSize, int tableSize) {
        this.cellSize = cellSize;
        this.tableSize = tableSize;
        register = new List[tableSize];
    }

    public void register(List<Entity> entities) {
        for (final var entity : entities) {
            final long gridX = (long) Math.floor(entity.position().x() / cellSize);
            final long gridY = (long) Math.floor(entity.position().y() / cellSize);

            int index = mixToKey(gridX, gridY);
            registerToKey(entity, index);
        }
    }

    private void registerToKey(Entity entity, int key) {
        List<Entity> reg = register[key];
        if (reg == null) {
            reg = new ArrayList<>();
        }
        reg.add(entity);
        register[key]  = reg;
    }


    public List<Entity> findAllNeighbors(Vector position) {
        final List<Entity> found = new ArrayList<>();
        final long gridX = (long) Math.floor(position.x() / cellSize);
        final long gridY = (long) Math.floor(position.y() / cellSize);

        // Hier passiert die Magie: Wir suchen in 9 Zellen
        for (long x = gridX - 1; x <= gridX + 1; x++) {
            for (long y = gridY - 1; y <= gridY + 1; y++) {
                List<Entity> c = register[mixToKey(x, y)];
                if (c != null) {
                    found.addAll(c);
                }
            }
        }
        return found;
    }
    private int mixToKey(long x, long y) {
        long h = (x * 73856093L) ^ (y * 19349663L);
        return (int) (h & (tableSize - 1));
    }
}
