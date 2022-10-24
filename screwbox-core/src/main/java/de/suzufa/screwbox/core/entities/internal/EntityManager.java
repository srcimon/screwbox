package de.suzufa.screwbox.core.entities.internal;

import java.util.List;

import de.suzufa.screwbox.core.entities.Archetype;
import de.suzufa.screwbox.core.entities.Entity;

public interface EntityManager {

    void addEntity(final Entity entity);

    List<Entity> allEntities();

    List<Entity> entitiesMatching(Archetype componentGroup);

    void delayChanges();

    void pickUpChanges();

    void removeEntity(Entity entity);

    Entity findById(int id);

}
