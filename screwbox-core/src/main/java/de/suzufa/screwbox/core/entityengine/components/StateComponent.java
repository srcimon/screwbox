package de.suzufa.screwbox.core.entityengine.components;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.entityengine.Component;
import de.suzufa.screwbox.core.entityengine.Entity;
import de.suzufa.screwbox.core.entityengine.EntityState;

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
