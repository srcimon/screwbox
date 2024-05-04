package io.github.srcimon.screwbox.examples.platformer.specials.player;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.assets.Asset;
import io.github.srcimon.screwbox.core.audio.Sound;
import io.github.srcimon.screwbox.core.audio.SoundBundle;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.logic.EntityState;
import io.github.srcimon.screwbox.core.environment.rendering.RenderComponent;
import io.github.srcimon.screwbox.core.graphics.Sprite;
import io.github.srcimon.screwbox.core.scenes.IntroAnimationBundle;
import io.github.srcimon.screwbox.core.scenes.SceneTransition;
import io.github.srcimon.screwbox.examples.platformer.components.CurrentLevelComponent;
import io.github.srcimon.screwbox.examples.platformer.components.DeathEventComponent;
import io.github.srcimon.screwbox.examples.platformer.components.PlayerControlComponent;
import io.github.srcimon.screwbox.examples.platformer.scenes.DeadScene;

import java.io.Serial;

import static io.github.srcimon.screwbox.core.scenes.ExtroAnimationBundle.CIRCLES;
import static io.github.srcimon.screwbox.tiled.Tileset.spriteAssetFromJson;

public class PlayerDeathState implements EntityState {

    @Serial
    private static final long serialVersionUID = 1L;

    private static final Asset<Sprite> SPRITE = spriteAssetFromJson("tilesets/specials/player.json", "dead");

    private static final Asset<Sound> OUCH_SOUND = Sound.assetFromFile("sounds/ouch.wav");
    private static final Asset<Sound> BLUPP_SOUND = Sound.assetFromFile("sounds/blupp.wav");

    @Override
    public void enter(Entity entity, Engine engine) {
        entity.remove(PlayerControlComponent.class);
        switch (entity.get(DeathEventComponent.class).deathType) {
            case WATER -> engine.audio().playSound(BLUPP_SOUND);
            case LAVA -> engine.audio().playSound(SoundBundle.ZISCH);
            default -> engine.audio().playSound(OUCH_SOUND);
        }

        entity.get(RenderComponent.class).sprite = SPRITE.get().freshInstance();
        String currentLevel = engine.environment().fetchSingletonComponent(CurrentLevelComponent.class).name;
        engine.scenes()
                .add(new DeadScene(currentLevel))
                .switchTo(DeadScene.class, SceneTransition
                        .extroAnimation(CIRCLES)
                        .extroDurationMillis(2000)
                        .introAnimation(IntroAnimationBundle.FADE_FROM_BLACK)
                        .introDurationMillis(250));

    }

    @Override
    public EntityState update(Entity entity, Engine engine) {
        return this;
    }

}
