package dev.screwbox.playground;

import dev.screwbox.core.Duration;
import dev.screwbox.core.Engine;
import dev.screwbox.core.Time;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.rendering.RenderComponent;
import dev.screwbox.core.graphics.AutoTile;
import dev.screwbox.core.graphics.Offset;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class AutoTileSystem implements EntitySystem {

    private static final Archetype AUTO_TILES = Archetype.ofSpacial(AutoTileComponent.class, RenderComponent.class);

    @Override
    public void update(Engine engine) {
        Time t = Time.now();
        final List<Entity> autoTiles = engine.environment().fetchAll(AUTO_TILES);
        Map<Offset, AutoTile> index = new HashMap<>();

        for (final var entity : autoTiles) {
            index.put(toGrid(entity.position(), 16), entity.get(AutoTileComponent.class).tile);
        }
        for (final var entity : autoTiles) {
            AutoTileComponent autoTile = entity.get(AutoTileComponent.class);
            Offset offset = toGrid(entity.position(), autoTile.cellSize);
            AutoTile.Mask mask = AutoTile.createMask(offset, o -> Objects.equals(index.get(o), autoTile.tile));
            if(!Objects.equals(mask, autoTile.mask)) {
                var sprite = autoTile.tile.findSprite(mask);
                entity.get(RenderComponent.class).sprite = sprite;
                autoTile.mask = mask;
            }



        }
        System.out.println(Duration.since(t).nanos());
    }

    private static Offset toGrid(final Vector position, int cellSize) {
        return Offset.at(toGrid(position.x(), cellSize), toGrid(position.y(), cellSize));
    }

    private static int toGrid(final double value, int cellSize) {
        return Math.floorDiv((int) value, cellSize);
    }
}
