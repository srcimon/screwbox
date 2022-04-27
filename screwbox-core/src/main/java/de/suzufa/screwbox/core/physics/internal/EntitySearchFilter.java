package de.suzufa.screwbox.core.physics.internal;

import de.suzufa.screwbox.core.entityengine.Entity;

public interface EntitySearchFilter {

    boolean matches(Entity entity);
}
