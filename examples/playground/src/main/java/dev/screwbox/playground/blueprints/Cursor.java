package dev.screwbox.playground.blueprints;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Percent;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.imports.ImportContext;
import dev.screwbox.core.environment.imports.SimpleBlueprint;
import dev.screwbox.core.environment.physics.CursorAttachmentComponent;
import dev.screwbox.core.environment.physics.TailwindComponent;

public class Cursor implements SimpleBlueprint {

    @Override
    public Entity create(final ImportContext context) {
        return new Entity()
                .bounds(Bounds.atOrigin(0, 0, 16, 16))
                .add(new CursorAttachmentComponent())
                .add(new TailwindComponent(40, Percent.max()));
    }
}
