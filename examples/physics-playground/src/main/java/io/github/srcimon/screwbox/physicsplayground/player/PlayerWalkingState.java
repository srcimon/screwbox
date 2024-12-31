package io.github.srcimon.screwbox.physicsplayground.player;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.audio.Playback;
import io.github.srcimon.screwbox.core.audio.Sound;
import io.github.srcimon.screwbox.core.audio.SoundOptions;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.logic.EntityState;
import io.github.srcimon.screwbox.core.environment.physics.PhysicsComponent;
import io.github.srcimon.screwbox.physicsplayground.tiles.Material;

import java.util.Map;

public class PlayerWalkingState implements EntityState {

    private Playback footsteps;
    private Material lastMaterial;

    private static final Map<Material, Sound> FOOTSTEP_SOUNDS = Map.of(
            Material.UNKNOWN, Sound.fromFile("stone.wav"),
            Material.WOOD, Sound.fromFile("wood.wav"),
            Material.STONE, Sound.fromFile("stone.wav")
    );

    @Override
    public void enter(Entity entity, Engine engine) {
        lastMaterial = getMaterial(entity, engine);
        footsteps = engine.audio().playSound(FOOTSTEP_SOUNDS.get(lastMaterial), SoundOptions.playContinuously());
    }

    @Override
    public EntityState update(Entity entity, Engine engine) {
        var material = getMaterial(entity, engine);

        if(material != lastMaterial) {
            engine.audio().stopPlayback(footsteps);
            footsteps = engine.audio().playSound(FOOTSTEP_SOUNDS.get(material), SoundOptions.playContinuously());
        }

        if (entity.get(PhysicsComponent.class).momentum.isZero()) {
            return new PlayerStandingState();
        }
        return this;
    }

    @Override
    public void exit(Entity entity, Engine engine) {
        engine.audio().stopPlayback(footsteps);
    }

    private Material getMaterial(Entity entity, Engine engine) {
        return engine.physics().raycastFrom(entity.position())
                .checkingFor(Archetype.ofSpacial(MaterialComponent.class))
                .castingVertical(40)
                .nearestEntity().map(hit -> hit.get(MaterialComponent.class).material)
                .orElse(Material.UNKNOWN);
    }
}
