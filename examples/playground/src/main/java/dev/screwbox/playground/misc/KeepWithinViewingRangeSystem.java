package dev.screwbox.playground.misc;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Engine;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.physics.PhysicsComponent;

public class KeepWithinViewingRangeSystem implements EntitySystem {

    private static final Archetype TARGETS = Archetype.ofSpacial(PhysicsComponent.class);

    @Override
    public void update(Engine engine) {
//        var visibleArea = engine.graphics().visibleArea();
//
//        for (var target : engine.environment().fetchAll(TARGETS)) {
//            Bounds bounds = target.bounds();
//            if (!visibleArea.contains(bounds)) {
//                double newX = target.position().x();
//                double newY = target.position().y();
//
//                if (bounds.maxX() < visibleArea.minX()) {
//                    newX += visibleArea.width() + bounds.width();
//                } else if (bounds.minX() > visibleArea.maxX()) {
//                    newX -= visibleArea.width() + bounds.width();
//                }
//
//                if (bounds.maxY() < visibleArea.minY()) {
//                    newY += visibleArea.height() + bounds.height();
//                } else if (bounds.minY() > visibleArea.maxY()) {
//                    newY -= visibleArea.height() + bounds.height();
//                }
//
//                target.moveTo(Vector.of(newX, newY));
//            }
//        }
    }
}
