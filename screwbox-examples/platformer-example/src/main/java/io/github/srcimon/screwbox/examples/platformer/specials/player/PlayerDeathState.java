package io.github.srcimon.screwbox.examples.platformer.specials.player;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Time;
import io.github.srcimon.screwbox.core.assets.Asset;
import io.github.srcimon.screwbox.core.audio.Sound;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.logic.EntityState;
import io.github.srcimon.screwbox.core.environment.rendering.RenderComponent;
import io.github.srcimon.screwbox.core.environment.rendering.ScreenTransitionComponent;
import io.github.srcimon.screwbox.core.graphics.Sprite;
import io.github.srcimon.screwbox.examples.platformer.components.DeathEventComponent;
import io.github.srcimon.screwbox.examples.platformer.components.PlayerControlComponent;
import io.github.srcimon.screwbox.examples.platformer.components.ResetSceneComponent;
import io.github.srcimon.screwbox.examples.platformer.components.TextComponent;
import io.github.srcimon.screwbox.core.graphics.transitions.*;

import java.io.Serial;
import java.util.List;

import static io.github.srcimon.screwbox.core.utils.ListUtil.randomFrom;
import static io.github.srcimon.screwbox.tiled.Tileset.spriteAssetFromJson;

public class PlayerDeathState implements EntityState {

    @Serial
    private static final long serialVersionUID = 1L;

    private static final Asset<Sprite> SPRITE = spriteAssetFromJson("tilesets/specials/player.json", "dead");

    private static final Asset<Sound> OUCH_SOUND = Sound.assetFromFile("sounds/ouch.wav");
    private static final Asset<Sound> BLUPP_SOUND = Sound.assetFromFile("sounds/blupp.wav");
    private static final Asset<Sound> ZISCH_SOUND = Sound.assetFromFile("sounds/zisch.wav");

    private static final List<ScreenTransition> TRANSITIONS = List.of(
            new FadeOutTransition(new HorizontalLinesTransition(20)),
            new FadeOutTransition(new SwipeTransition()),
            new FadeOutTransition(new FadingScreenTransition()),
            new FadeOutTransition(new CircleTransition()),
            new FadeOutTransition(new MosaikTransition(30, 20)));

    @Override
    public void enter(Entity entity, Engine engine) {
        entity.remove(PlayerControlComponent.class);
        switch (entity.get(DeathEventComponent.class).deathType) {
            case WATER -> engine.audio().playEffect(BLUPP_SOUND);
            case LAVA -> engine.audio().playEffect(ZISCH_SOUND);
            default -> engine.audio().playEffect(OUCH_SOUND);
        }

        entity.get(RenderComponent.class).sprite = SPRITE.get().freshInstance();
        entity.add(new ScreenTransitionComponent(randomFrom(TRANSITIONS), Duration.ofSeconds(3)));
        entity.add(new TextComponent("GAME OVER", ""));
        entity.add(new ResetSceneComponent(Time.now().plusSeconds(3)));
    }

    @Override
    public EntityState update(Entity entity, Engine engine) {
        return this;
    }

}
