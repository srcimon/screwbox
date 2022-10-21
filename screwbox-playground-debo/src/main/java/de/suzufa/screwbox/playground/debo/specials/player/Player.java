package de.suzufa.screwbox.playground.debo.specials.player;

import de.suzufa.screwbox.core.Bounds;
import de.suzufa.screwbox.core.entities.Entity;
import de.suzufa.screwbox.core.entities.SourceImport.Converter;
import de.suzufa.screwbox.core.entities.components.AutoFlipSpriteComponent;
import de.suzufa.screwbox.core.entities.components.ColliderComponent;
import de.suzufa.screwbox.core.entities.components.CollisionSensorComponent;
import de.suzufa.screwbox.core.entities.components.LightGlowComponent;
import de.suzufa.screwbox.core.entities.components.PhysicsBodyComponent;
import de.suzufa.screwbox.core.entities.components.PointLightComponent;
import de.suzufa.screwbox.core.entities.components.SignalComponent;
import de.suzufa.screwbox.core.entities.components.SpotLightComponent;
import de.suzufa.screwbox.core.entities.components.SpriteComponent;
import de.suzufa.screwbox.core.entities.components.StateComponent;
import de.suzufa.screwbox.core.entities.components.TransformComponent;
import de.suzufa.screwbox.core.graphics.Color;
import de.suzufa.screwbox.playground.debo.components.CastShadowComponent;
import de.suzufa.screwbox.playground.debo.components.GroundDetectorComponent;
import de.suzufa.screwbox.playground.debo.components.PlayerControlComponent;
import de.suzufa.screwbox.playground.debo.components.PlayerMarkerComponent;
import de.suzufa.screwbox.tiled.GameObject;

public class Player implements Converter<GameObject> {

    @Override
    public Entity convert(final GameObject object) {
        return new Entity(object.id()).add(
                new LightGlowComponent(60, Color.WHITE.opacity(0.15)),
                new PointLightComponent(120, Color.BLACK),
                new SpotLightComponent(120, Color.BLACK.opacity(0.4)),
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
