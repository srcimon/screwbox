package io.github.simonbas.screwbox.examples.platformer.specials.player;

import io.github.simonbas.screwbox.core.Bounds;
import io.github.simonbas.screwbox.core.entities.Entity;
import io.github.simonbas.screwbox.core.entities.SourceImport.Converter;
import io.github.simonbas.screwbox.core.entities.components.*;
import io.github.simonbas.screwbox.core.graphics.Color;
import io.github.simonbas.screwbox.core.graphics.LightOptions;
import io.github.simonbas.screwbox.examples.platformer.components.CastShadowComponent;
import io.github.simonbas.screwbox.examples.platformer.components.GroundDetectorComponent;
import io.github.simonbas.screwbox.examples.platformer.components.PlayerControlComponent;
import io.github.simonbas.screwbox.examples.platformer.components.PlayerMarkerComponent;
import io.github.simonbas.screwbox.tiled.GameObject;

public class Player implements Converter<GameObject> {

    @Override
    public Entity convert(final GameObject object) {
        return new Entity(object.id()).add(
                new PointLightComponent(LightOptions.glowing(64)
                        .color(Color.BLACK)
                        .glow(0.5)),
                new SpotLightComponent(LightOptions.glowing(64)
                        .color(Color.BLACK.opacity(0.4))
                        .glow(0)),
                new StateComponent(new PlayerStandingState()),
                new PhysicsBodyComponent(),
                new GroundDetectorComponent(),
                new ColliderComponent(),
                new PlayerMarkerComponent(),
                new RenderComponent(object.layer().order()),
                new CastShadowComponent(),
                new PlayerControlComponent(),
                new SignalComponent(),
                new CollisionSensorComponent(),
                new AutoFlipSpriteComponent(),
                new TransformComponent(Bounds.atPosition(object.position(), 10, 24)));
    }

}
