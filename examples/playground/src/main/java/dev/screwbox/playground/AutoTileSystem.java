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
    public void update(Engine engine) {
        Time t = Time.now();
        final List<Entity> autoTiles = engine.environment().fetchAll(AUTO_TILES);
        Map<Offset, AutoTile> index = new HashMap<>();

        for (final var entity : autoTiles) {
            index.put(Grid.findCell(entity.position(), 16), entity.get(AutoTileComponent.class).tile);
        }
        for (final var entity : autoTiles) {
            AutoTileComponent autoTile = entity.get(AutoTileComponent.class);
            Offset offset = Grid.findCell(entity.position(), autoTile.cellSize);
            AutoTile.Mask mask = AutoTile.createMask(offset, o -> Objects.equals(index.get(o), autoTile.tile));
            if (!Objects.equals(mask, autoTile.mask)) {
                var sprite = autoTile.tile.findSprite(mask);
                entity.get(RenderComponent.class).sprite = sprite;
                autoTile.mask = mask;
            }


        }
        System.out.println(Duration.since(t).nanos());
    }
}
