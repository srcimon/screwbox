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
            int key = calcKey(entity.position());
            List<Entity> reg = register[key];
            if(reg == null) {
                reg = new ArrayList<>();
            }
            reg.add(entity);
        }
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
        return Math.abs((int)(x * 73856093L + y * 19349663L)) % TABLE_SIZE;
    }
}
