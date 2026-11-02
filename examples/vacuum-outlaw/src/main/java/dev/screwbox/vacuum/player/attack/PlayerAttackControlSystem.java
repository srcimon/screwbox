package dev.screwbox.vacuum.player.attack;

import dev.screwbox.core.Engine;
import dev.screwbox.core.assets.Asset;
import dev.screwbox.core.audio.SoundBundle;
import dev.screwbox.core.audio.SoundOptions;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.core.TransformComponent;
import dev.screwbox.core.environment.light.GlowComponent;
import dev.screwbox.core.environment.light.PointLightComponent;
import dev.screwbox.core.environment.logic.StateComponent;
import dev.screwbox.core.environment.physics.CollisionSensorComponent;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.core.environment.rendering.RenderComponent;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.ShaderBundle;
import dev.screwbox.core.graphics.Sprite;
import dev.screwbox.core.graphics.options.SpriteDrawOptions;
import dev.screwbox.core.mouse.MouseButton;

public class PlayerAttackControlSystem implements EntitySystem {

    private static final Archetype PLAYER = Archetype.ofSpacial(PlayerAttackControlComponent.class);
    private static final Asset<Sprite> SHOOT = Asset.asset(() -> Sprite.fromFile("tilesets/objects/shot_small.png").compileShader(ShaderBundle.CHROMATIC_ABERRATION));

    @Override
    public void update(Engine engine) {
        engine.environment().tryFetchSingleton(PLAYER).ifPresent(player -> {
            final var attackControl = player.get(PlayerAttackControlComponent.class);
            boolean gunIsLoaded = attackControl.reloadDuration.addTo(attackControl.lastShotFired).isBefore(engine.loop().time());
            if (engine.mouse().isDown(MouseButton.LEFT) && gunIsLoaded) {
                attackControl.lastShotFired = engine.loop().time();
                engine.audio().playSound(SoundBundle.PHASER, SoundOptions.playOnce().position(player.position()));
                final int drawOrder = player.get(RenderComponent.class).options.drawOrder();
                engine.environment().addEntity("shoot",
                    new TransformComponent(player.position(), 8, 8),
                    new GlowComponent(8, Color.RED.opacity(0.75)),
                    new PointLightComponent(64, Color.RED),
                    new ShotComponent(),
                    new CollisionSensorComponent(),
                    new StateComponent(new ShotUnderwayState()),
                    new PhysicsComponent(engine.mouse().position().subtract(player.position()).length(120)),
                    new RenderComponent(SHOOT, SpriteDrawOptions.originalSize().shaderSetup(ShaderBundle.CHROMATIC_ABERRATION).drawOrder(drawOrder)));
            }
        });
    }
}
