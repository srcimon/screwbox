package dev.screwbox.core.environment.physics;

import dev.screwbox.core.Engine;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.mouse.Mouse;

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
