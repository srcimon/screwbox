package dev.screwbox.playground.misc;

import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpacialHash {
    private final Map<Long, List<Entity>> register = new HashMap<>();

    private final double cellSize;

    public SpacialHash(double cellSize) {
        this.cellSize = cellSize;
    }

    private static final ArrayList<Entity> EMPTY = new ArrayList<>();

    public void register(List<Entity> entities) {
        for (final var entity : entities) {
            register.computeIfAbsent(calcKey(entity.position()), e -> new ArrayList<>()).add(entity);
        }
    }

    public List<Entity> findSingleCells(Vector position) {
        final long gridX = (long) Math.floor(position.x() / cellSize);
        final long gridY = (long) Math.floor(position.y() / cellSize);
        final long key = mix(gridX, gridY);
        return register.getOrDefault(key, EMPTY);
    }
    public List<Entity> find(Vector position) {
        final List<Entity> found = new ArrayList<>(64); // Vorallokation
        final long gridX = (long) Math.floor(position.x() / cellSize);
        final long gridY = (long) Math.floor(position.y() / cellSize);

        // Direkte Key-Berechnung in der Schleife ohne Vector-Objekte
        for (long x = gridX - 1; x <= gridX + 1; x++) {
            for (long y = gridY - 1; y <= gridY + 1; y++) {
                final long key = mix(x, y);
                final var cell = register.get(key);
                if (cell != null) {
                    found.addAll(cell);
                }
            }
        }
        return found;
    }

    private long mix(long x, long y) {
        // Cantor Pairing oder Primzahlen-Mix für stabilere Keys
        return x * 73856093L ^ y * 19349663L;
    }

    private long calcKey(Vector position) {
        // Wichtig: Erst durch cellSize teilen und abrunden, um ein Grid zu erzeugen
        long x = (long) Math.floor(position.x() / cellSize);
        long y = (long) Math.floor(position.y() / cellSize);
        // Klassische Hash-Kombination für 2D-Grids
        return x * 73856093L ^ y * 19349663L;
    }
}
