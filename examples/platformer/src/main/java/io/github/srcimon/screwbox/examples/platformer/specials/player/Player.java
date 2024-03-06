package io.github.srcimon.screwbox.examples.platformer.specials.player;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.SourceImport.Converter;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;
import io.github.srcimon.screwbox.core.environment.light.GlowComponent;
import io.github.srcimon.screwbox.core.environment.light.PointLightComponent;
import io.github.srcimon.screwbox.core.environment.light.SpotLightComponent;
import io.github.srcimon.screwbox.core.environment.logic.SignalComponent;
import io.github.srcimon.screwbox.core.environment.logic.StateComponent;
import io.github.srcimon.screwbox.core.environment.physics.ColliderComponent;
import io.github.srcimon.screwbox.core.environment.physics.CollisionDetectionComponent;
import io.github.srcimon.screwbox.core.environment.physics.PhysicsComponent;
import io.github.srcimon.screwbox.core.environment.rendering.CameraTargetComponent;
import io.github.srcimon.screwbox.core.environment.rendering.FlipSpriteComponent;
import io.github.srcimon.screwbox.core.environment.rendering.RenderComponent;
import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.examples.platformer.components.CastShadowComponent;
import io.github.srcimon.screwbox.examples.platformer.components.GroundDetectorComponent;
import io.github.srcimon.screwbox.examples.platformer.components.PlayerControlComponent;
import io.github.srcimon.screwbox.examples.platformer.components.PlayerMarkerComponent;
import io.github.srcimon.screwbox.tiled.GameObject;

import static io.github.srcimon.screwbox.core.graphics.LightOptions.radius;

public class Player implements Converter<GameObject> {

    @Override
    public Entity convert(final GameObject object) {
        return new Entity(object.id(), "Player").add(
                new CameraTargetComponent(),
                new GlowComponent(radius(32).color(Color.WHITE.opacity(0.1))),
                new PointLightComponent(radius(64).color(Color.BLACK)),
                new SpotLightComponent(radius(64).color(Color.BLACK.opacity(0.4))),
                new StateComponent(new PlayerStandingState()),
                new PhysicsComponent(),
                new GroundDetectorComponent(),
                new ColliderComponent(),
                new PlayerMarkerComponent(),
                new RenderComponent(object.layer().order()),
                new CastShadowComponent(),
                new PlayerControlComponent(),
                new SignalComponent(),
                new CollisionDetectionComponent(),
                new FlipSpriteComponent(),
                new TransformComponent(Bounds.atPosition(object.position(), 10, 24)));
    }

}
