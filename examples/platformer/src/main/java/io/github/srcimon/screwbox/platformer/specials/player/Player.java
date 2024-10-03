package io.github.srcimon.screwbox.platformer.specials.player;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Ease;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.SourceImport.Converter;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;
import io.github.srcimon.screwbox.core.environment.light.GlowComponent;
import io.github.srcimon.screwbox.core.environment.light.PointLightComponent;
import io.github.srcimon.screwbox.core.environment.light.SpotLightComponent;
import io.github.srcimon.screwbox.core.environment.logic.SignalComponent;
import io.github.srcimon.screwbox.core.environment.logic.StateComponent;
import io.github.srcimon.screwbox.core.environment.particles.ParticleEmitterComponent;
import io.github.srcimon.screwbox.core.environment.physics.ColliderComponent;
import io.github.srcimon.screwbox.core.environment.physics.CollisionDetectionComponent;
import io.github.srcimon.screwbox.core.environment.physics.PhysicsComponent;
import io.github.srcimon.screwbox.core.environment.rendering.CameraTargetComponent;
import io.github.srcimon.screwbox.core.environment.rendering.FlipSpriteComponent;
import io.github.srcimon.screwbox.core.environment.rendering.RenderComponent;
import io.github.srcimon.screwbox.core.environment.tweening.TweenComponent;
import io.github.srcimon.screwbox.core.environment.tweening.TweenSpinComponent;
import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.platformer.components.CastShadowComponent;
import io.github.srcimon.screwbox.platformer.components.GroundDetectorComponent;
import io.github.srcimon.screwbox.platformer.components.PlayerControlComponent;
import io.github.srcimon.screwbox.platformer.components.PlayerMarkerComponent;
import io.github.srcimon.screwbox.tiled.GameObject;
import io.github.srcimon.screwbox.tiled.Tileset;

import static io.github.srcimon.screwbox.core.environment.particles.ParticleEmitterComponent.SpawnMode.POSITION;
import static io.github.srcimon.screwbox.core.particles.ParticleOptions.unknownSource;

public class Player implements Converter<GameObject> {

    @Override
    public Entity convert(final GameObject object) {
        return new Entity(object.id(), "Player")
                .addCustomized(new ParticleEmitterComponent(Duration.ofMillis(220), POSITION, unknownSource()
                                .sprites(Tileset.fromJson("tilesets/effects/smokes.json").all())
                                .baseSpeed(Vector.y(-5))
                                .lifetimeMilliseconds(300)
                                .animateOpacity()),
                        emitter -> emitter.isEnabled = false)
                .add(new CameraTargetComponent(),
                        new GlowComponent(45, Color.WHITE.opacity(0.3)),
                        new PointLightComponent(64, Color.BLACK),
                        new SpotLightComponent(64, Color.BLACK.opacity(0.4)),
                        new StateComponent(new PlayerStandingState()),
                        new TweenComponent(Duration.ofSeconds(2), Ease.LINEAR_IN, true),
                        new TweenSpinComponent(0.4,0.4),
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
