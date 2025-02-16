package io.github.srcimon.screwbox.playground.scene.enemy;

import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.SourceImport;
import io.github.srcimon.screwbox.core.environment.ai.PatrolMovementComponent;
import io.github.srcimon.screwbox.core.environment.physics.PhysicsComponent;
import io.github.srcimon.screwbox.core.environment.rendering.RenderComponent;
import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.Sprite;
import io.github.srcimon.screwbox.core.utils.AsciiMap;

public class PatrollingEnemy implements SourceImport.Converter<AsciiMap.Tile> {

    @Override
    public Entity convert(AsciiMap.Tile tile) {
        return new Entity().name("patrolling-enemy")
                .bounds(tile.bounds())
                .add(new PhysicsComponent())
                .add(new RenderComponent(Sprite.placeholder(Color.RED, tile.size())))
                .add(new PatrolMovementComponent(20));
    }
}
