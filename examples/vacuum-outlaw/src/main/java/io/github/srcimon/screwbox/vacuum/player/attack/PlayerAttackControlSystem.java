package io.github.srcimon.screwbox.vacuum.player.attack;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.assets.Asset;
import io.github.srcimon.screwbox.core.audio.SoundBundle;
import io.github.srcimon.screwbox.core.audio.SoundOptions;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;
import io.github.srcimon.screwbox.core.environment.light.GlowComponent;
import io.github.srcimon.screwbox.core.environment.light.SpotLightComponent;
import io.github.srcimon.screwbox.core.environment.logic.StateComponent;
import io.github.srcimon.screwbox.core.environment.physics.CollisionSensorComponent;
import io.github.srcimon.screwbox.core.environment.physics.PhysicsComponent;
import io.github.srcimon.screwbox.core.environment.rendering.RenderComponent;
import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.Sprite;
import io.github.srcimon.screwbox.core.mouse.MouseButton;

public class PlayerAttackControlSystem implements EntitySystem {

    private static final Archetype PLAYER = Archetype.ofSpacial(PlayerAttackControlComponent.class);
    private static final Asset<Sprite> SHOOT = Sprite.assetFromFile("tilesets/objects/shot_small.png");

    @Override
    public void update(Engine engine) {
        engine.environment().tryFetchSingleton(PLAYER).ifPresent(player -> {
            final var attackControl = player.get(PlayerAttackControlComponent.class);
            boolean gunIsLoaded = attackControl.reloadDuration.addTo(attackControl.lastShotFired).isBefore(engine.loop().time());
            if (engine.mouse().isDown(MouseButton.LEFT) && gunIsLoaded) {
                attackControl.lastShotFired = engine.loop().time();
                engine.audio().playSound(SoundBundle.PHASER, SoundOptions.playOnce().position(player.position()));
                engine.environment().addEntity("shoot",
                        new TransformComponent(player.position(), 8, 8),
                        new GlowComponent(8, Color.WHITE.opacity(0.75)),
                        new SpotLightComponent(16, Color.BLACK),
                        new ShotComponent(),
                        new CollisionSensorComponent(),
                        new StateComponent(new ShotUnderwayState()),
                        new PhysicsComponent(engine.mouse().position().substract(player.position()).length(120)),
                        new RenderComponent(SHOOT, player.get(RenderComponent.class).drawOrder));
            }
        });
    }
}
