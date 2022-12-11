package de.suzufa.screwbox.examples.platformer.systems;

import java.util.Optional;

import de.suzufa.screwbox.core.Bounds;
import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.Segment;
import de.suzufa.screwbox.core.Vector;
import de.suzufa.screwbox.core.entities.Archetype;
import de.suzufa.screwbox.core.entities.Entity;
import de.suzufa.screwbox.core.entities.EntitySystem;
import de.suzufa.screwbox.core.entities.Order;
import de.suzufa.screwbox.core.entities.SystemOrder;
import de.suzufa.screwbox.core.entities.components.RenderComponent;
import de.suzufa.screwbox.core.entities.components.TransformComponent;
import de.suzufa.screwbox.core.utils.MathUtil;
import de.suzufa.screwbox.examples.platformer.components.FollowPlayerComponent;
import de.suzufa.screwbox.examples.platformer.components.PlayerMarkerComponent;

@Order(SystemOrder.SIMULATION_BEGIN)
public class FollowPlayerSystem implements EntitySystem {

    private static final Archetype PLAYER = Archetype.of(PlayerMarkerComponent.class, TransformComponent.class,
            RenderComponent.class);
    private static final Archetype FOLLOWING = Archetype.of(FollowPlayerComponent.class, TransformComponent.class);

    @Override
    public void update(Engine engine) {
        Optional<Entity> playerEntity = engine.entities().fetch(PLAYER);
        if (playerEntity.isEmpty()) {
            return;
        }
        Entity player = playerEntity.get();
        var playerPosition = player.get(TransformComponent.class).bounds.position();

        for (Entity followEntity : engine.entities().fetchAll(FOLLOWING)) {
            var followComponent = followEntity.get(FollowPlayerComponent.class);
            final TransformComponent followTransform = followEntity.get(TransformComponent.class);

            Segment lineBetweenFollowerAndPlayer = Segment.between(followTransform.bounds.position(),
                    playerPosition);
            double x = MathUtil.clamp(followComponent.speed * -1,
                    lineBetweenFollowerAndPlayer.to().x() - lineBetweenFollowerAndPlayer.from().x(),
                    followComponent.speed);

            double y = MathUtil.clamp(followComponent.speed * -1,
                    lineBetweenFollowerAndPlayer.to().y() - lineBetweenFollowerAndPlayer.from().y(),
                    followComponent.speed);

            Vector movement = Vector.of(x, y).multiply(engine.loop().delta());
            Bounds updatedBounds = followTransform.bounds.moveBy(movement);
            followTransform.bounds = updatedBounds;
        }
    }
}
