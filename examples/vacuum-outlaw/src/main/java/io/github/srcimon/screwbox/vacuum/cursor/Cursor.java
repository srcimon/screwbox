package io.github.srcimon.screwbox.vacuum.cursor;

import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.assets.Asset;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.SourceImport;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;
import io.github.srcimon.screwbox.core.environment.physics.CursorAttachmentComponent;
import io.github.srcimon.screwbox.core.environment.rendering.RenderComponent;
import io.github.srcimon.screwbox.core.graphics.Sprite;
import io.github.srcimon.screwbox.tiled.Map;

import static io.github.srcimon.screwbox.core.assets.Asset.asset;
import static io.github.srcimon.screwbox.core.graphics.SpriteDrawOptions.scaled;

public class Cursor implements SourceImport.Converter<Map> {

    private static final Asset<Sprite> CURSOR = asset(() -> Sprite.fromFile("tilesets/objects/cursor.png").scaled(3));

    @Override
    public Entity convert(final Map map) {
        //TODO Reload animation
        return new Entity().name("cursor")
                .add(new TransformComponent(Vector.zero(), 16, 16))
                .addCustomized(new RenderComponent(CURSOR, Integer.MAX_VALUE, scaled(0.3)),
                        render -> render.renderOverLight = true)
                .add(new CursorAttachmentComponent());
    }
}
