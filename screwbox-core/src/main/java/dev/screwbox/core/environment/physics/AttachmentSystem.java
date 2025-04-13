package dev.screwbox.core.environment.physics;

import dev.screwbox.core.Engine;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.core.TransformComponent;

/**
 * Moves all {@link Entity entities} containing {@link AttachmentComponent} to the specified {@link Entity} they
 * are attached to.
 */
public class AttachmentSystem implements EntitySystem {

    private static final Archetype ATTACHED_ENTITIES = Archetype.of(AttachmentComponent.class, TransformComponent.class);

    @Override
    public void update(final Engine engine) {
        for (final var entity : engine.environment().fetchAll(ATTACHED_ENTITIES)) {
            final var attachment = entity.get(AttachmentComponent.class);
            engine.environment().tryFetchById(attachment.targetId).ifPresent(
                    target -> entity.moveTo(target.position().add(attachment.offset)));
        }
    }
}
