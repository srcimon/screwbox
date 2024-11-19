package io.github.srcimon.screwbox.platformer.systems;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Line;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.Order;
import io.github.srcimon.screwbox.core.environment.rendering.RenderComponent;
import io.github.srcimon.screwbox.platformer.components.FollowPlayerComponent;
import io.github.srcimon.screwbox.platformer.components.PlayerMarkerComponent;

import java.util.Optional;

@Order(Order.SystemOrder.SIMULATION_EARLY)
public class FollowPlayerSystem implements EntitySystem {

    private static final Archetype PLAYER = Archetype.ofSpacial(PlayerMarkerComponent.class, RenderComponent.class);
    private static final Archetype FOLLOWING = Archetype.ofSpacial(FollowPlayerComponent.class);

    @Override
    public void update(Engine engine) {
        Optional<Entity> playerEntity = engine.environment().tryFetchSingleton(PLAYER);
        if (playerEntity.isEmpty()) {
            return;
        }
        Entity player = playerEntity.get();
        var playerPosition = player.position();

        for (Entity followEntity : engine.environment().fetchAll(FOLLOWING)) {
            var followComponent = followEntity.get(FollowPlayerComponent.class);
            Line lineBetweenFollowerAndPlayer = Line.between(followEntity.position(), playerPosition);
            double x = Math.clamp(lineBetweenFollowerAndPlayer.to().x() - lineBetweenFollowerAndPlayer.from().x(), followComponent.speed * -1, followComponent.speed);

            double y = Math.clamp(lineBetweenFollowerAndPlayer.to().y() - lineBetweenFollowerAndPlayer.from().y(), followComponent.speed * -1, followComponent.speed);

            Vector movement = Vector.of(x, y).multiply(engine.loop().delta());
            followEntity.moveBy(movement);
        }
    }
}
