package dev.screwbox.core.environment.logic;

import dev.screwbox.core.Engine;
import dev.screwbox.core.environment.Entity;

import java.io.Serial;

class InitialEntityState implements EntityState {

    @Serial
    private static final long serialVersionUID = 1L;

    private final EntityState nextState;

    InitialEntityState(EntityState nextState) {
        this.nextState = nextState;
    }

    @Override
    public EntityState update(Entity entity, Engine engine) {
        return nextState;
    }
}
