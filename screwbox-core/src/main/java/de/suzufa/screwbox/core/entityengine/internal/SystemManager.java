package de.suzufa.screwbox.core.entityengine.internal;

import java.util.List;

import de.suzufa.screwbox.core.entityengine.EntitySystem;

public interface SystemManager {

    void addSystem(EntitySystem system);

    List<EntitySystem> allSystems();

    void updateAllSystems();

    void removeSystem(Class<? extends EntitySystem> systemType);

    boolean isSystemPresent(Class<? extends EntitySystem> type);
}
