package io.github.srcimon.screwbox.physicsplayground.player.states;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.audio.Playback;
import io.github.srcimon.screwbox.core.audio.Sound;
import io.github.srcimon.screwbox.core.audio.SoundOptions;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.logic.EntityState;
import io.github.srcimon.screwbox.core.environment.physics.PhysicsComponent;
import io.github.srcimon.screwbox.core.keyboard.Key;
import io.github.srcimon.screwbox.physicsplayground.player.PlayerInfoComponent;
import io.github.srcimon.screwbox.physicsplayground.tiles.Material;

import java.util.Map;
import java.util.Objects;

public class PlayerWalkingState implements EntityState {

    private Playback footsteps;
    private Material lastMaterial;

    private static final Map<Material, Sound> FOOTSTEP_SOUNDS = Map.of(
            Material.STONE, Sound.fromFile("player/stone.wav"),
            Material.WOOD, Sound.fromFile("player/wood.wav")
    );

    @Override
    public void enter(Entity entity, Engine engine) {
        lastMaterial = entity.get(PlayerInfoComponent.class).currentGroundMaterial;
        footsteps = playMaterialSound(engine, lastMaterial);
    }

    @Override
    public EntityState update(Entity entity, Engine engine) {
        var material = entity.get(PlayerInfoComponent.class).currentGroundMaterial;

        if (material != lastMaterial) {
            if(footsteps != null) {
                engine.audio().stopPlayback(footsteps);
            }
            lastMaterial = material;
            footsteps = playMaterialSound(engine, material);
        }

        if(engine.keyboard().isPressed(Key.SPACE)) {
            return new PlayerJumpingState();
        }
        if (entity.get(PhysicsComponent.class).momentum.isZero()) {
            return new PlayerStandingState();
        }
        return this;
    }

    private Playback playMaterialSound(Engine engine, Material material) {
        Sound sound = FOOTSTEP_SOUNDS.get(material);
        if (Objects.nonNull(sound)) {
            return engine.audio().playSound(sound, SoundOptions.playContinuously());
        }
        return null;
    }

    @Override
    public void exit(Entity entity, Engine engine) {
        if(footsteps != null) {
            engine.audio().stopPlayback(footsteps);
        }
    }
}
