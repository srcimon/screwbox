package de.suzufa.screwbox.core.entityengine.internal;

import java.util.List;

import de.suzufa.screwbox.core.entityengine.Archetype;
import de.suzufa.screwbox.core.entityengine.Entity;

public interface EntityManager {

    void addEntity(final Entity entity);

    List<Entity> allEntities();

    List<Entity> entitiesMatching(Archetype componentGroup);

    void delayChanges();

    void pickUpChanges();

    void removeEntity(Entity entity);

    Entity findById(int id);
}
