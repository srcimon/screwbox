package de.suzufa.screwbox.playground.debo.specials.player;

import static de.suzufa.screwbox.core.utils.ListUtil.randomFrom;

import java.util.List;

import de.suzufa.screwbox.core.Duration;
import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.Time;
import de.suzufa.screwbox.core.entities.Entity;
import de.suzufa.screwbox.core.entities.EntityState;
import de.suzufa.screwbox.core.entities.components.ScreenTransitionComponent;
import de.suzufa.screwbox.core.entities.components.SpriteComponent;
import de.suzufa.screwbox.core.graphics.transitions.CircleTransition;
import de.suzufa.screwbox.core.graphics.transitions.FadeOutTransition;
import de.suzufa.screwbox.core.graphics.transitions.FadingScreenTransition;
import de.suzufa.screwbox.core.graphics.transitions.HorizontalLinesTransition;
import de.suzufa.screwbox.core.graphics.transitions.MosaikTransition;
import de.suzufa.screwbox.core.graphics.transitions.ScreenTransition;
import de.suzufa.screwbox.core.graphics.transitions.SwipeTransition;
import de.suzufa.screwbox.playground.debo.components.DeathEventComponent;
import de.suzufa.screwbox.playground.debo.components.PlayerControlComponent;
import de.suzufa.screwbox.playground.debo.components.ResetSceneComponent;
import de.suzufa.screwbox.playground.debo.components.TextComponent;

public class PlayerDeathState implements EntityState {

    private static final long serialVersionUID = 1L;

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
        case WATER:
            engine.audio().playEffect(PlayerResources.BLUPP_SOUND);
            break;
        case LAVA:
            engine.audio().playEffect(PlayerResources.ZISCH_SOUND);
            break;
        default:
            engine.audio().playEffect(PlayerResources.OUCH_SOUND);
        }

        entity.get(SpriteComponent.class).sprite = PlayerResources.DEAD_SPRITE.newInstance();
        entity.add(new ScreenTransitionComponent(randomFrom(TRANSITIONS), Duration.ofSeconds(3)));
        entity.add(new TextComponent("GAME OVER", ""));
        entity.add(new ResetSceneComponent(Time.now().plusSeconds(3)));
    }

    @Override
    public EntityState update(Entity entity, Engine engine) {
        return this;
    }

}
