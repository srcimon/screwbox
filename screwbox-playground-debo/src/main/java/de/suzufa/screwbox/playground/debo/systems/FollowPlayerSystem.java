package de.suzufa.screwbox.playground.debo.systems;

import java.util.Optional;

import de.suzufa.screwbox.core.Bounds;
import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.Segment;
import de.suzufa.screwbox.core.Vector;
import de.suzufa.screwbox.core.entityengine.Archetype;
import de.suzufa.screwbox.core.entityengine.Entity;
import de.suzufa.screwbox.core.entityengine.EntitySystem;
import de.suzufa.screwbox.core.entityengine.UpdatePriority;
import de.suzufa.screwbox.core.entityengine.components.TransformComponent;
import de.suzufa.screwbox.core.entityengine.components.SpriteComponent;
import de.suzufa.screwbox.core.utils.MathUtil;
import de.suzufa.screwbox.playground.debo.components.FollowPlayerComponent;
import de.suzufa.screwbox.playground.debo.components.PlayerMarkerComponent;

public class FollowPlayerSystem implements EntitySystem {

    private static final Archetype PLAYER = Archetype.of(PlayerMarkerComponent.class, TransformComponent.class,
            SpriteComponent.class);
    private static final Archetype FOLLOWING = Archetype.of(FollowPlayerComponent.class, TransformComponent.class);

    @Override
    public void update(Engine engine) {
        Optional<Entity> playerEntity = engine.entityEngine().fetch(PLAYER);
        if (playerEntity.isEmpty()) {
            return;
        }
        Entity player = playerEntity.get();
        var playerPosition = player.get(TransformComponent.class).bounds.position();

        for (Entity followEntity : engine.entityEngine().fetchAll(FOLLOWING)) {
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

            Vector movement = Vector.of(x, y).multiply(engine.loop().metrics().delta());
            Bounds updatedBounds = followTransform.bounds.moveBy(movement);
            followTransform.bounds = updatedBounds;
        }
    }

    @Override
    public UpdatePriority updatePriority() {
        return UpdatePriority.SIMULATION_BEGIN;
    }
}
