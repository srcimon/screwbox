package dev.screwbox.playground.misc;

import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpacialHash {
    static final int TABLE_SIZE = 8192;

    private final List<Entity>[] register = new List[TABLE_SIZE];

    private final double cellSize;

    public SpacialHash(double cellSize) {
        this.cellSize = cellSize;
    }

    public void register(List<Entity> entities) {
        for (final var entity : entities) {
            final long gridX = (long) Math.floor(entity.position().x() / cellSize);
            final long gridY = (long) Math.floor(entity.position().y() / cellSize);

            // Sucht in der aktuellen Zelle und allen 8 Nachbarn
            for (long x = gridX - 1; x <= gridX + 1; x++) {
                for (long y = gridY - 1; y <= gridY + 1; y++) {
                    int index = mixToKey(x, y);
                    registerToKey(entity, index);
                }
            }

        }
    }

    private void registerToKey(Entity entity, int key) {
        List<Entity> reg = register[key];
        if(reg == null) {
            reg = new ArrayList<>();
        }
        reg.add(entity);
    }

    public List<Entity> findSingleCells(Vector position) {
        var key = calcKey(position);
        List<Entity> entities = register[key];
        if(entities == null) {
            return Collections.EMPTY_LIST;
        }
        return entities;
    }

    private int calcKey(Vector position) {
        long x = (long) Math.floor(position.x() / cellSize);
        long y = (long) Math.floor(position.y() / cellSize);
        return mixToKey(x, y);
    }

    private static int mixToKey(long x, long y) {
        return Math.abs((int) (x * 73856093L + y * 19349663L)) % TABLE_SIZE;
    }
}
