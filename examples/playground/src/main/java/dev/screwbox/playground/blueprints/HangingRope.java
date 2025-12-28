package dev.screwbox.playground.blueprints;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.fluids.FloatComponent;
import dev.screwbox.core.environment.imports.ImportContext;
import dev.screwbox.core.environment.imports.ComplexBlueprint;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.core.environment.softphysics.RopeComponent;
import dev.screwbox.core.environment.softphysics.RopeRenderComponent;
import dev.screwbox.core.environment.softphysics.SoftLinkComponent;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.utils.TileMap;

import java.util.ArrayList;
import java.util.List;

public class HangingRope implements ComplexBlueprint<TileMap.Tile<Character>> {
    @Override
    public List<Entity> create(TileMap.Tile<Character> source, ImportContext context) {
        Vector end = source.position().addY(50);
        int count = 8;
        Vector start = source.position();
        Vector spacing = end.substract(start).multiply(1.0 / count);
        List<Entity> entities = new ArrayList<>();
        int id = context.allocateId();
        for (int i = count; i >= 0; i--) {
            Entity add = new Entity(id)
                    .name(i == count ? "start" : "node")
                    .add(new FloatComponent())
                    .bounds(Bounds.atPosition(start.add(spacing.multiply(i)), 4, 4))
                    .add(new PhysicsComponent(), p -> p.friction = 2);


            if (i == count) {
                add.add(new RopeComponent());
                add.add(new RopeRenderComponent(Color.ORANGE, 4));
            }
            if (i != 0) {
                add.add(new SoftLinkComponent(context.peekId()));
            }
            entities.add(add);
            id = context.allocateId();
        }
        return entities;
    }
}
