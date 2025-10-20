package dev.screwbox.playground;

import dev.screwbox.core.Duration;
import dev.screwbox.core.Engine;
import dev.screwbox.core.Time;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.rendering.RenderComponent;
import dev.screwbox.core.graphics.AutoTile;
import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.navigation.Grid;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class AutoTileSystem implements EntitySystem {

    private static final Archetype AUTO_TILES = Archetype.ofSpacial(AutoTileComponent.class, RenderComponent.class);
    
    @Override
    public void update(final Engine engine) {
        //TODO add sheduler here
        Time t = Time.now();
        final List<Entity> autoTiles = engine.environment().fetchAll(AUTO_TILES);
        final Map<Offset, AutoTile> index = new HashMap<>();

        for (final var entity : autoTiles) {
            final AutoTile autoTile = entity.get(AutoTileComponent.class).tile;
            final Offset cell = Grid.findCell(entity.position(), autoTile.width()); // AutoTiles are always square
            index.put(cell, autoTile);
        }
        for (final var entity : autoTiles) {
            final var autoTile = entity.get(AutoTileComponent.class);
            final var offset = Grid.findCell(entity.position(), autoTile.tile.width()); // AutoTiles are always square
            final var mask = AutoTile.createMask(offset, o -> Objects.equals(index.get(o), autoTile.tile));
            if (!Objects.equals(mask, autoTile.mask)) {
                var sprite = autoTile.tile.findSprite(mask);
                entity.get(RenderComponent.class).sprite = sprite;
                autoTile.mask = mask;
            }


        }
        System.out.println(Duration.since(t).nanos());
    }

    //TODO create some kind of cache index here to prevent recalculation without change
}
