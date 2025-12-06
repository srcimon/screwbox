package dev.screwbox.playground;

import dev.screwbox.core.Engine;
import dev.screwbox.core.Polygon;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.ExecutionOrder;
import dev.screwbox.core.environment.Order;
import dev.screwbox.core.environment.softphysics.SoftBodyComponent;
import dev.screwbox.core.environment.softphysics.SoftLinkComponent;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.options.OvalDrawOptions;
import dev.screwbox.core.graphics.options.PolygonDrawOptions;
import dev.screwbox.core.utils.ListUtil;

import java.util.ArrayList;

import static dev.screwbox.core.environment.Order.SIMULATION_PREPARE;

@ExecutionOrder(SIMULATION_PREPARE)
public class SoftBodyCollisionSystem implements EntitySystem {

    private static final Archetype BODIES = Archetype.ofSpacial(SoftBodyComponent.class, SoftLinkComponent.class);

    record Check(Entity first, Entity second) {

    }

    @Override
    public void update(Engine engine) {
        final var bodies = engine.environment().fetchAll(BODIES);
//        for(var body : bodies) {
//            if(toPolygon(body).contains(engine.mouse().position())) {
//                engine.graphics().world().drawPolygon(toPolygon(body).nodes(), PolygonDrawOptions.outline(Color.RED).drawOrder(99999999));
//            }
//        }
        final var checks = ListUtil.uniqueCombinations(bodies, Check::new);
        for(final var check : checks) {
            Polygon first = toPolygon(check.first);
            Polygon second = toPolygon(check.second);
            extracted(engine, first, second);
            extracted(engine, second, first);
        }
    }

    private static void extracted(Engine engine, Polygon first, Polygon second) {
        for(final var node : first.nodes()) {
            if(second.contains(node)) {
                engine.graphics().world().drawCircle(node, 2, OvalDrawOptions.filled(Color.MAGENTA).drawOrder(Order.DEBUG_OVERLAY.drawOrder()));
            }
        }
    }

    private static Polygon toPolygon(Entity entity) {
        return Polygon.ofNodes(entity.get(SoftBodyComponent.class).nodes.stream().map(Entity::position).toList());
    }
}
