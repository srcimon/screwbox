package dev.screwbox.playground;

import dev.screwbox.core.Duration;
import dev.screwbox.core.Engine;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.Order;
import dev.screwbox.core.environment.rendering.RenderComponent;
import dev.screwbox.core.graphics.AutoTile;
import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.navigation.Grid;
import dev.screwbox.core.utils.Scheduler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Order(Order.SystemOrder.PRESENTATION_PREPARE)
public class AutoTileSystem implements EntitySystem {

    private static final Archetype AUTO_TILES = Archetype.ofSpacial(AutoTileComponent.class, RenderComponent.class);
    private static final Scheduler SCHEDULER = Scheduler.withInterval(Duration.ofMillis(50));

    record TileIndexEntry(Offset cell, Entity entity, AutoTileComponent autoTile) {

    }

    private double cacheHash = 0;
    //TODO Scheduler?

    @Override
    public void update(final Engine engine) {
        // if(SCHEDULER.isTick(engine.loop().time())) {
        double currentHash = 0;
        final List<Entity> autoTiles = engine.environment().fetchAll(AUTO_TILES);
        final Map<Offset, TileIndexEntry> index = new HashMap<>();

        for (final var entity : autoTiles) {
            AutoTileComponent autoTileComponent = entity.get(AutoTileComponent.class);
            final AutoTile autoTile = autoTileComponent.tile;
            final Offset cell = Grid.findCell(entity.position(), autoTile.width()); // AutoTiles are always square
            index.put(cell, new TileIndexEntry(cell, entity, autoTileComponent));
            currentHash += cell.x() * 13 + cell.y() * 31;
        }
        if (currentHash != cacheHash) {
            cacheHash = currentHash;

            for (final var entry : index.values()) {
                final var mask = AutoTile.createMask(entry.cell, o -> {
                    final var indexEntry = index.get(o);
                    return Objects.nonNull(indexEntry) && Objects.equals(indexEntry.autoTile().tile, entry.autoTile.tile)
                           && Objects.equals(indexEntry.autoTile().cellSize, entry.autoTile.cellSize);
                });
                if (!Objects.equals(mask, entry.autoTile.mask)) {
                    entry.entity.get(RenderComponent.class).sprite = entry.autoTile.tile.findSprite(mask);
                    entry.autoTile.mask = mask;
                }
            }

        }
    }

}
