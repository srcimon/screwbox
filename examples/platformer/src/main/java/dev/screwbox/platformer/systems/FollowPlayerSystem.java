package dev.screwbox.platformer.systems;

import dev.screwbox.core.Engine;
import dev.screwbox.core.Line;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.ExecutionOrder;
import dev.screwbox.core.environment.Order;
import dev.screwbox.core.environment.rendering.RenderComponent;
import dev.screwbox.platformer.components.FollowPlayerComponent;
import dev.screwbox.platformer.components.PlayerMarkerComponent;

@ExecutionOrder(Order.SIMULATION_EARLY)
public class FollowPlayerSystem implements EntitySystem {

    private static final Archetype PLAYER = Archetype.ofSpacial(PlayerMarkerComponent.class, RenderComponent.class);
    private static final Archetype FOLLOWING = Archetype.ofSpacial(FollowPlayerComponent.class);

    @Override
    public void update(final Engine engine) {
        engine.environment().tryFetchSingleton(PLAYER).ifPresent(player -> {
            var playerPosition = player.position();

            for (Entity followEntity : engine.environment().fetchAll(FOLLOWING)) {
                var followComponent = followEntity.get(FollowPlayerComponent.class);
                Line lineBetweenFollowerAndPlayer = Line.between(followEntity.position(), playerPosition);
                double x = Math.clamp(lineBetweenFollowerAndPlayer.end().x() - lineBetweenFollowerAndPlayer.start().x(), followComponent.speed * -1, followComponent.speed);
                double y = Math.clamp(lineBetweenFollowerAndPlayer.end().y() - lineBetweenFollowerAndPlayer.start().y(), followComponent.speed * -1, followComponent.speed);

                Vector movement = Vector.of(x, y).multiply(engine.loop().delta());
                followEntity.moveBy(movement);
            }
        });
    }
}
