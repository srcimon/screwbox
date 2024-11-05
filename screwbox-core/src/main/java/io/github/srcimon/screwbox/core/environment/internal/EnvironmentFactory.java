package io.github.srcimon.screwbox.core.environment.internal;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.Order;

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

    public void addRenderingEngineComponent(final RenderingSubsystem component) {
        Order.SystemOrder order = component.getClass().getAnnotation(Order.class).value();
        virtualSystems.add(new VirtualSystem(order, engine -> component.render()));
    }
}
