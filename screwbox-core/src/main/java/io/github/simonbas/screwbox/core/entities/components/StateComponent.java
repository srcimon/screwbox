package io.github.simonbas.screwbox.core.entities.components;

import io.github.simonbas.screwbox.core.Engine;
import io.github.simonbas.screwbox.core.entities.Component;
import io.github.simonbas.screwbox.core.entities.Entity;
import io.github.simonbas.screwbox.core.entities.EntityState;

public final class StateComponent implements Component {

    private static final long serialVersionUID = 1L;

    public EntityState state;

    public StateComponent(final EntityState state) {
        this.state = new EntityState() {// ensures the state.enter()-Method is invoked on initial state

            private static final long serialVersionUID = 1L;

            @Override
            public EntityState update(Entity entity, Engine engine) {
                return state;
            }
        };
    }
}
