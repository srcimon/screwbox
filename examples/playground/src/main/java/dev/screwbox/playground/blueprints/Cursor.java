package dev.screwbox.playground.blueprints;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Percent;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.blueprints.Blueprint;
import dev.screwbox.core.environment.physics.CursorAttachmentComponent;
import dev.screwbox.core.environment.physics.TailwindComponent;
import dev.screwbox.core.utils.TileMap;

public class Cursor implements Blueprint<TileMap<Character>> {

    @Override
    public Entity create(final TileMap<Character> map) {
        return new Entity()
                .bounds(Bounds.atOrigin(map.bounds().position(), 16, 16))
                .add(new CursorAttachmentComponent())
                .add(new TailwindComponent(40, Percent.max()));
    }
}
