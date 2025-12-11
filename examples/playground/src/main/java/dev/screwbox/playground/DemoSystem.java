package dev.screwbox.playground;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Engine;
import dev.screwbox.core.Polygon;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.Order;
import dev.screwbox.core.environment.softphysics.SoftBodyComponent;
import dev.screwbox.core.graphics.options.LineDrawOptions;
import dev.screwbox.core.graphics.options.OvalDrawOptions;
import dev.screwbox.core.keyboard.Key;

import java.util.ArrayList;
import java.util.List;

import static dev.screwbox.core.graphics.Color.RED;
import static dev.screwbox.core.graphics.Color.WHITE;

public class DemoSystem implements EntitySystem {
    @Override
    public void update(Engine engine) {
        for(var body : engine.environment().fetchAll(Archetype.ofSpacial(SoftBodyComponent.class))) {
            var polygon = toPolygon(body.get(SoftBodyComponent.class));
            var nearest = engine.mouse().position().nearestOf(polygon.nodes());
            var index = polygon.indexOf(nearest);
            polygon.opposingIndex(index).ifPresent(opposingIndex -> {
                var opposing = polygon.node(opposingIndex);
                if(Bounds.around(polygon.nodes()).expand(20).contains(engine.mouse().position())) {
                    engine.graphics().world().drawCircle(nearest, 2, OvalDrawOptions.filled(WHITE).drawOrder(Order.DEBUG_OVERLAY.drawOrder()+999));
                    engine.graphics().world().drawCircle(opposing, 2, OvalDrawOptions.filled(WHITE).drawOrder(Order.DEBUG_OVERLAY.drawOrder()+999));
                    engine.graphics().world().drawLine(nearest, opposing, LineDrawOptions.color(WHITE).strokeWidth(1).drawOrder(Order.DEBUG_OVERLAY.drawOrder()+999));
                }
            });
        }

    }

    private static Polygon toPolygon(final SoftBodyComponent component) {
        final List<Vector> list = new ArrayList<>();
        for (final var node : component.nodes) {
            list.add(node.position());
        }
        return Polygon.ofNodes(list);
    }
}
