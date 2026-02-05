package dev.screwbox.platformer.specials.player;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Duration;
import dev.screwbox.core.Vector;
import dev.screwbox.core.assets.Asset;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.core.TransformComponent;
import dev.screwbox.core.environment.importing.Blueprint;
import dev.screwbox.core.environment.light.GlowComponent;
import dev.screwbox.core.environment.light.PointLightComponent;
import dev.screwbox.core.environment.light.SpotLightComponent;
import dev.screwbox.core.environment.logic.StateComponent;
import dev.screwbox.core.environment.particles.ParticleEmitterComponent;
import dev.screwbox.core.environment.physics.ColliderComponent;
import dev.screwbox.core.environment.physics.CollisionDetailsComponent;
import dev.screwbox.core.environment.physics.CollisionSensorComponent;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.core.environment.rendering.CameraTargetComponent;
import dev.screwbox.core.environment.rendering.FlipSpriteComponent;
import dev.screwbox.core.environment.rendering.RenderComponent;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.particles.ParticleOptions;
import dev.screwbox.platformer.components.CastShadowComponent;
import dev.screwbox.platformer.components.PlayerControlComponent;
import dev.screwbox.platformer.components.PlayerMarkerComponent;
import dev.screwbox.tiled.GameObject;
import dev.screwbox.tiled.Tileset;

import static dev.screwbox.core.particles.SpawnMode.POSITION;

public class Player implements Blueprint<GameObject> {

    private static final Asset<ParticleOptions> SMOKE_PARTICLE = Asset.asset(() -> ParticleOptions.unknownSource()
        .sprites(Tileset.fromJson("tilesets/effects/smokes.json").all())
        .baseSpeed(Vector.y(-5))
        .lifespanMilliseconds(300)
        .animateOpacity());

    @Override
    public Entity assembleFrom(final GameObject object) {
        return new Entity(object.id(), "Player")
            .add(new ParticleEmitterComponent(Duration.ofMillis(220), POSITION, SMOKE_PARTICLE),
                emitter -> emitter.isEnabled = false)
            .add(new CameraTargetComponent(),
                new GlowComponent(45, Color.WHITE.opacity(0.3)),
                new PointLightComponent(64, Color.BLACK),
                new SpotLightComponent(64, Color.BLACK.opacity(0.4)),
                new StateComponent(new PlayerStandingState()),
                new PhysicsComponent(),
                new CollisionDetailsComponent(),
                new ColliderComponent(),
                new PlayerMarkerComponent(),
                new RenderComponent(object.layer().order()),
                new CastShadowComponent(),
                new PlayerControlComponent(),
                new CollisionSensorComponent(),
                new FlipSpriteComponent(),
                new TransformComponent(Bounds.atPosition(object.position(), 10, 24)));
    }

}
