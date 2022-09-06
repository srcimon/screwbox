package de.suzufa.screwbox.playground.debo.specials.player;

import de.suzufa.screwbox.core.Bounds;
import de.suzufa.screwbox.core.entityengine.Entity;
import de.suzufa.screwbox.core.entityengine.SourceImport.Converter;
import de.suzufa.screwbox.core.entityengine.components.AutoFlipSpriteComponent;
import de.suzufa.screwbox.core.entityengine.components.ColliderComponent;
import de.suzufa.screwbox.core.entityengine.components.CollisionSensorComponent;
import de.suzufa.screwbox.core.entityengine.components.PhysicsBodyComponent;
import de.suzufa.screwbox.core.entityengine.components.SignalComponent;
import de.suzufa.screwbox.core.entityengine.components.SpriteComponent;
import de.suzufa.screwbox.core.entityengine.components.StateComponent;
import de.suzufa.screwbox.core.entityengine.components.TransformComponent;
import de.suzufa.screwbox.core.entityengine.components.WaterReflectionComponent;
import de.suzufa.screwbox.playground.debo.components.CastShadowComponent;
import de.suzufa.screwbox.playground.debo.components.GroundDetectorComponent;
import de.suzufa.screwbox.playground.debo.components.PlayerControlComponent;
import de.suzufa.screwbox.playground.debo.components.PlayerMarkerComponent;
import de.suzufa.screwbox.tiled.GameObject;

public class Player implements Converter<GameObject> {

    @Override
    public Entity convert(final GameObject object) {
        return new Entity(object.id()).add(
                new WaterReflectionComponent(),
                new StateComponent(new PlayerStandingState()),
                new PhysicsBodyComponent(),
                new GroundDetectorComponent(),
                new ColliderComponent(),
                new PlayerMarkerComponent(),
                new SpriteComponent(object.layer().order()),
                new CastShadowComponent(),
                new PlayerControlComponent(),
                new SignalComponent(),
                new CollisionSensorComponent(),
                new AutoFlipSpriteComponent(),
                new TransformComponent(Bounds.atPosition(object.position(), 10, 24)));
    }

}
