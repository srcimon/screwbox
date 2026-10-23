package dev.screwbox.playground;

import dev.screwbox.core.environment.Component;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.ExecutionOrder;
import dev.screwbox.core.environment.Order;

import java.util.ArrayList;
import java.util.List;

@ExecutionOrder(Order.PREPARATION)
public class StashComponent implements Component {

    public final List<Entity> stashedEntities = new ArrayList<>();
}
