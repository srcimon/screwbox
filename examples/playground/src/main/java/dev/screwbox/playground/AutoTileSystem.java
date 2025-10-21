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

    private record TileIndexEntry(Offset cell, Entity entity, AutoTileComponent component) {

    }

    private double lastHash = 0; // hash is used to prevent unnecessary update loops

    @Override
    public void update(final Engine engine) {
        if (SCHEDULER.isTick(engine.loop().time())) {
            final List<Entity> autoTiles = engine.environment().fetchAll(AUTO_TILES);
            final Map<Offset, TileIndexEntry> index = new HashMap<>();
            final double hash = updateAutoTileIndex(autoTiles, index);
            if (hash != lastHash) {
                lastHash = hash;
                updateSprites(index);
            }
        }
    }

    private void updateSprites(final Map<Offset, TileIndexEntry> index) {
        for (final var indexEntry : index.values()) {
            final var mask = AutoTile.createMask(indexEntry.cell, cell -> {
                final var compareEntry = index.get(cell);
                return Objects.nonNull(compareEntry) && Objects.equals(compareEntry.component.tile, indexEntry.component.tile);
            });
            if (!Objects.equals(mask, indexEntry.component.mask)) {
                indexEntry.entity.get(RenderComponent.class).sprite = indexEntry.component.tile.findSprite(mask);
                indexEntry.component.mask = mask;
            }
        }
    }

    private double updateAutoTileIndex(final List<Entity> autoTiles, final Map<Offset, TileIndexEntry> index) {
        double hash = 0;
        for (final var entity : autoTiles) {
            AutoTileComponent autoTileComponent = entity.get(AutoTileComponent.class);
            final AutoTile autoTile = autoTileComponent.tile;
            final Offset cell = Grid.findCell(entity.position(), autoTile.width()); // AutoTiles are always square
            index.put(cell, new TileIndexEntry(cell, entity, autoTileComponent));
            hash += cell.x() * 3.1 + cell.y() * 5.21;
        }
        return hash;
    }

}
