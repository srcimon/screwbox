package dev.screwbox.platformer.enemies.tracer;

import dev.screwbox.core.Engine;
import dev.screwbox.core.assets.Asset;
import dev.screwbox.core.audio.Sound;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.audio.SoundComponent;
import dev.screwbox.core.environment.logic.EntityState;
import dev.screwbox.core.environment.rendering.RenderComponent;
import dev.screwbox.core.graphics.ShaderBundle;
import dev.screwbox.core.graphics.Sprite;
import dev.screwbox.core.graphics.ShaderSetup;
import dev.screwbox.platformer.components.DetectLineOfSightToPlayerComponent;
import dev.screwbox.platformer.components.FollowPlayerComponent;
import dev.screwbox.tiled.Tileset;

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
        renderComponent.options = renderComponent.options.shaderSetup(ShaderBundle.ALARMED);
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
        renderComponent.options = renderComponent.options.shaderSetup((ShaderSetup) null);
        entity.remove(FollowPlayerComponent.class);
        entity.remove(SoundComponent.class);
        engine.audio().stopAllPlaybacks(SOUND);
    }

}