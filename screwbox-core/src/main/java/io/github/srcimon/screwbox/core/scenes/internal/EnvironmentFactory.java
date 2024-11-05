package io.github.srcimon.screwbox.core.scenes.internal;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.Order;
import io.github.srcimon.screwbox.core.environment.internal.DefaultEnvironment;

import java.util.ArrayList;
import java.util.List;

public class EnvironmentFactory {

    record VirtualSystem(Order.SystemOrder order, EntitySystem system) {
    }

    private List<VirtualSystem> virtualSystems = new ArrayList<>();

    public DefaultEnvironment createEnvironment(final Engine engine) {
        final DefaultEnvironment environment = new DefaultEnvironment(engine);
        for (var virtualSystem : virtualSystems) {
            environment.addSystem(virtualSystem.order, virtualSystem.system);
        }
        return environment;
    }

    public void registerVirtualSystem(final Order.SystemOrder order, final Runnable runnable) {
        virtualSystems.add(new VirtualSystem(order, engine -> runnable.run()));
    }
}
