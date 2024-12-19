package io.github.srcimon.screwbox.core.environment.physics;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.mouse.Mouse;

/**
 * Moves all {@link Entity entities} having a {@link CursorAttachmentComponent} to the current mouse cursor position.
 *
 * @see Mouse#position()
 */
public class CursorAttachmentSystem implements EntitySystem {

    private static final Archetype CURSOR_ATTACHED = Archetype.ofSpacial(CursorAttachmentComponent.class);

    @Override
    public void update(final Engine engine) {
        for (final var attached : engine.environment().fetchAll(CURSOR_ATTACHED)) {
            final var offset = attached.get(CursorAttachmentComponent.class).offset;
            attached.moveTo(engine.mouse().position().add(offset));
        }
    }
}
