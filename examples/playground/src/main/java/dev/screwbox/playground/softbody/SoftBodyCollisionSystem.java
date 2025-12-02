package dev.screwbox.playground.softbody;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Engine;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.Order;
import dev.screwbox.core.environment.softphysics.SoftLinkComponent;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.options.RectangleDrawOptions;

import java.awt.*;
import java.awt.geom.Area;

public class SoftBodyCollisionSystem implements EntitySystem {

    private static final Archetype SOFTBODIES = Archetype.ofSpacial(SoftbodyComponent.class, SoftLinkComponent.class);
    record Item(Entity entity, Area area) {

    }
    @Override
    public void update(Engine engine) {
        var items = engine.environment().fetchAll(SOFTBODIES).stream().map(b -> new Item(b, toArea(b))).toList();
        for(var item : items) {
            for(var target : items) {
                if(item!=target) {
                    Area clone = new Area(item.area);
                    clone.intersect(target.area);
                    boolean collided = !clone.isEmpty();
                    if(collided) {
                        engine.graphics().world().drawRectangle(Bounds.atOrigin(clone.getBounds().x, clone.getBounds().y, clone.getBounds().width, clone.getBounds().height), RectangleDrawOptions.filled(Color.BLUE).drawOrder(Order.PRESENTATION_UI.drawOrder()));
                    }
                }
            }
        }
    }

    private Area toArea(Entity b) {
        Polygon poly = new Polygon();
        b.get(SoftbodyComponent.class).nodes.stream().forEach(n -> poly.addPoint((int)n.position().x(), (int)n.position().y()));
        return new Area(poly);
    }
}
