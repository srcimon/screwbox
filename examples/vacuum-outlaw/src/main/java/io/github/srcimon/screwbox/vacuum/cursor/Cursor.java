package io.github.srcimon.screwbox.vacuum.cursor;

import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.SourceImport;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;
import io.github.srcimon.screwbox.core.environment.physics.CursorAttachmentComponent;
import io.github.srcimon.screwbox.core.environment.rendering.RenderComponent;
import io.github.srcimon.screwbox.core.graphics.Sprite;
import dev.screwbox.tiles.Map;

public class Cursor implements SourceImport.Converter<Map> {

    @Override
    public Entity convert(final Map map) {

        return new Entity().name("cursor")
                .add(new TransformComponent(Vector.zero(), 16, 16))
                .add(new DynamicCursorImageComponent())
                .add(new RenderComponent(Sprite.invisible(), Integer.MAX_VALUE),
                        render -> render.renderInForeground = true)
                .add(new CursorAttachmentComponent());
    }
}
