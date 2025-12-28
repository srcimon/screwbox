package dev.screwbox.vacuum.cursor;

import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Blueprint;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.SourceImport;
import dev.screwbox.core.environment.core.TransformComponent;
import dev.screwbox.core.environment.physics.CursorAttachmentComponent;
import dev.screwbox.core.environment.rendering.RenderComponent;
import dev.screwbox.core.graphics.Sprite;
import dev.screwbox.tiled.Map;

import static dev.screwbox.core.environment.Order.PRESENTATION_UI;

public class Cursor implements Blueprint<Map> {

    @Override
    public Entity create(Map source) {
        return new Entity().name("cursor")
                .add(new TransformComponent(Vector.zero(), 16, 16))
                .add(new DynamicCursorImageComponent())
                .add(new RenderComponent(Sprite.invisible(), PRESENTATION_UI.drawOrder()))
                .add(new CursorAttachmentComponent());
    }
}
