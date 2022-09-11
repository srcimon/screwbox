package de.suzufa.screwbox.core.physics.internal;

import de.suzufa.screwbox.core.entities.Entity;

public interface EntitySearchFilter {

    boolean matches(Entity entity);
}
