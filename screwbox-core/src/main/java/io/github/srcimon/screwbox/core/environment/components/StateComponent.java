package io.github.srcimon.screwbox.core.environment.components;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.Component;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.EntityState;

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
