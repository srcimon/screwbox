package dev.screwbox.core.environment.rendering;

import dev.screwbox.core.Engine;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.Order;
import dev.screwbox.core.graphics.AutoTile;
import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.navigation.Grid;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Updates {@link RenderComponent} of {@link Entity entities} with {@link AutoTileComponent}.
 *
 * @since 3.12.0
 */
//TODO document in docs
@Order(Order.SystemOrder.PRESENTATION_PREPARE)
public class AutoTileSystem implements EntitySystem {

    private static final Archetype AUTO_TILES = Archetype.ofSpacial(AutoTileComponent.class, RenderComponent.class);

    private record TileIndexEntry(Offset cell, Entity entity, AutoTileComponent component) {

    }

    private double lastHash = 0; // hash is used to prevent unnecessary update loops

    @Override
    public void update(final Engine engine) {
        final List<Entity> autoTiles = engine.environment().fetchAll(AUTO_TILES);
        final double hash = createHash(autoTiles);
        System.out.println(hash);
        if (hash != lastHash) {
            lastHash = hash;
            final Map<Offset, TileIndexEntry> index = buildIndex(autoTiles);
            updateSprites(index);
        }
    }

    private double createHash(final List<Entity> autoTiles) {
        double hash = 0;
        for (var e : autoTiles) {
            Vector position = e.position();
            hash += position.x() * 3.1 + position.y() * 5.21;
        }
        return hash;
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

    private Map<Offset, TileIndexEntry> buildIndex(final List<Entity> autoTiles) {
        final Map<Offset, TileIndexEntry> index = new HashMap<>();
        for (final var entity : autoTiles) {
            AutoTileComponent autoTileComponent = entity.get(AutoTileComponent.class);
            final AutoTile autoTile = autoTileComponent.tile;
            final Offset cell = Grid.findCell(entity.position(), autoTile.width()); // AutoTiles are always square
            index.put(cell, new TileIndexEntry(cell, entity, autoTileComponent));
        }
        return index;
    }

}
