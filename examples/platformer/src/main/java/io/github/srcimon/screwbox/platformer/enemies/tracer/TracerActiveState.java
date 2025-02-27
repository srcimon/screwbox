package io.github.srcimon.screwbox.platformer.enemies.tracer;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.assets.Asset;
import io.github.srcimon.screwbox.core.audio.Sound;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.audio.SoundComponent;
import io.github.srcimon.screwbox.core.environment.logic.EntityState;
import io.github.srcimon.screwbox.core.environment.rendering.RenderComponent;
import io.github.srcimon.screwbox.core.graphics.ShaderBundle;
import io.github.srcimon.screwbox.core.graphics.Sprite;
import io.github.srcimon.screwbox.core.graphics.drawoptions.ShaderOptions;
import io.github.srcimon.screwbox.platformer.components.DetectLineOfSightToPlayerComponent;
import io.github.srcimon.screwbox.platformer.components.FollowPlayerComponent;
import io.github.srcimon.screwbox.tiled.Tileset;

import java.io.Serial;

public class TracerActiveState implements EntityState {

    @Serial
    private static final long serialVersionUID = 1L;

    private static final Asset<Sprite> SPRITE = Tileset.spriteAssetFromJson("tilesets/enemies/tracer.json", "active");
    private static final Asset<Sound> SOUND = Sound.assetFromFile("sounds/scream.wav");

    @Override
    public void enter(Entity entity, Engine engine) {
        final var renderComponent = entity.get(RenderComponent.class);
        renderComponent.sprite = SPRITE.get().freshInstance();
        renderComponent.options = renderComponent.options.shaderOptions(ShaderBundle.FLASHING_RED);
        entity.add(new FollowPlayerComponent());
        entity.add(new SoundComponent(SOUND));
    }

    @Override
    public EntityState update(Entity entity, Engine engine) {
        return entity.get(DetectLineOfSightToPlayerComponent.class).isInLineOfSight
                ? this
                : new TracerInactiveState();
    }

    @Override
    public void exit(Entity entity, Engine engine) {
        final var renderComponent = entity.get(RenderComponent.class);
        renderComponent.options = renderComponent.options.noShader();
        entity.remove(FollowPlayerComponent.class);
        entity.remove(SoundComponent.class);
        engine.audio().stopAllPlaybacks(SOUND);
    }

}