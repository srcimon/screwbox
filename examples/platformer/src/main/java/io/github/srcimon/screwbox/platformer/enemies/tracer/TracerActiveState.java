package io.github.srcimon.screwbox.platformer.enemies.tracer;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.assets.Asset;
import io.github.srcimon.screwbox.core.audio.Playback;
import io.github.srcimon.screwbox.core.audio.Sound;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.logic.EntityState;
import io.github.srcimon.screwbox.core.environment.rendering.RenderComponent;
import io.github.srcimon.screwbox.core.graphics.Sprite;
import io.github.srcimon.screwbox.platformer.components.DetectLineOfSightToPlayerComponent;
import io.github.srcimon.screwbox.platformer.components.FollowPlayerComponent;
import io.github.srcimon.screwbox.tiled.Tileset;

import java.io.Serial;

import static io.github.srcimon.screwbox.core.audio.SoundOptions.playContinuously;
import static java.util.Objects.isNull;

public class TracerActiveState implements EntityState {

    @Serial
    private static final long serialVersionUID = 1L;

    private static final Asset<Sprite> SPRITE = Tileset.spriteAssetFromJson("tilesets/enemies/tracer.json", "active");
    private static final Asset<Sound> SOUND = Sound.assetFromFile("sounds/scream.wav");
    private Playback playback;

    @Override
    public void enter(Entity entity, Engine engine) {
        entity.get(RenderComponent.class).sprite = SPRITE.get().freshInstance();
        entity.add(new FollowPlayerComponent());

    }

    @Override
    public EntityState update(Entity entity, Engine engine) {
        if (isNull(playback) || !engine.audio().playbackIsActive(playback)) {
            playback = engine.audio().playSound(SOUND, playContinuously());
        }
        return entity.get(DetectLineOfSightToPlayerComponent.class).isInLineOfSight
                ? this
                : new TracerInactiveState();
    }

    @Override
    public void exit(Entity entity, Engine engine) {
        entity.remove(FollowPlayerComponent.class);
        engine.audio().stopAllPlaybacks(SOUND);
    }

}