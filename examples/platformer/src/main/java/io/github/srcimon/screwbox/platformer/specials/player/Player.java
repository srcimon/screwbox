package io.github.srcimon.screwbox.platformer.specials.player;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Duration;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.SourceImport.Converter;
import dev.screwbox.core.environment.core.TransformComponent;
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
import io.github.srcimon.screwbox.platformer.components.CastShadowComponent;
import io.github.srcimon.screwbox.platformer.components.PlayerControlComponent;
import io.github.srcimon.screwbox.platformer.components.PlayerMarkerComponent;
import dev.screwbox.tiles.GameObject;
import dev.screwbox.tiles.Tileset;

import static dev.screwbox.core.environment.particles.ParticleEmitterComponent.SpawnMode.POSITION;
import static dev.screwbox.core.particles.ParticleOptions.unknownSource;

public class Player implements Converter<GameObject> {

    @Override
    public Entity convert(final GameObject object) {
        return new Entity(object.id(), "Player")
                .add(new ParticleEmitterComponent(Duration.ofMillis(220), POSITION, unknownSource()
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
