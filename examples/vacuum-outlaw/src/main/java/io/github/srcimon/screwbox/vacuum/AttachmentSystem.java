package io.github.srcimon.screwbox.vacuum;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;
//TODO Make engine default
public class AttachmentSystem implements EntitySystem {

    private Archetype ATTACHED_ENTITIES = Archetype.of(AttachmentComponent.class, TransformComponent.class);

    @Override
    public void update(final Engine engine) {
        for(final var entity : engine.environment().fetchAll(ATTACHED_ENTITIES))  {
            final var attachment = entity.get(AttachmentComponent.class);
            entity.moveTo(engine.environment().fetchById(attachment.targetId).position().add(attachment.offset));
        }
    }
}
