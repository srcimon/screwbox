package dev.screwbox.playground.blueprints;

import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.AdvancedBlueprint;
import dev.screwbox.core.environment.ImportContext;
import dev.screwbox.core.environment.physics.GravityComponent;
import dev.screwbox.core.utils.TileMap;

public class Gravity implements AdvancedBlueprint<TileMap<Character>> {

    @Override
    public Entity create(final TileMap<Character> map, final ImportContext context) {
        return new Entity()
                .name("gravity")
                .add(new GravityComponent(Vector.y(400)));
    }
}
