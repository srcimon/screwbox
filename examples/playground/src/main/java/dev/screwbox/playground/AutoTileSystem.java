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

    record AAAA(Offset cell, Entity entity, AutoTileComponent autoTile) {

    }
    @Override
    public void update(final Engine engine) {
        //TODO add sheduler here
        Time t = Time.now();
        final List<Entity> autoTiles = engine.environment().fetchAll(AUTO_TILES);
        final Map<Offset, AAAA> index = new HashMap<>();

        for (final var entity : autoTiles) {
            AutoTileComponent autoTileComponent = entity.get(AutoTileComponent.class);
            final AutoTile autoTile = autoTileComponent.tile;
            final Offset cell = Grid.findCell(entity.position(), autoTile.width()); // AutoTiles are always square
            index.put(cell, new AAAA( cell, entity, autoTileComponent));
        }
        for (final var entry : index.values()) {
            final var autoTile = entry.autoTile.tile;
            final var offset =  entry.cell;
            final var mask = AutoTile.createMask(offset, o -> {
                AAAA aaaa = index.get(o);
                if(aaaa==null) {
                    return false;
                }
                return Objects.equals(aaaa.autoTile().tile, entry.autoTile.tile);
            });
            if (!Objects.equals(mask, entry.autoTile.mask)) {
                var sprite = autoTile.findSprite(mask);
                entry.entity.get(RenderComponent.class).sprite = sprite;
                entry.autoTile.mask = mask;
            }


        }
        System.out.println(Duration.since(t).nanos());
    }

    //TODO create some kind of cache index here to prevent recalculation without change
}
